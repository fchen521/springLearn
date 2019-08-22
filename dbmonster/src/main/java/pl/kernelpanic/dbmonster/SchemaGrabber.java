package pl.kernelpanic.dbmonster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.kernelpanic.dbmonster.connection.ConnectionProvider;
import pl.kernelpanic.dbmonster.connection.DBCPConnectionProvider;
import pl.kernelpanic.dbmonster.connection.Transaction;
import pl.kernelpanic.dbmonster.generator.BooleanGenerator;
import pl.kernelpanic.dbmonster.generator.DataGenerator;
import pl.kernelpanic.dbmonster.generator.DateTimeGenerator;
import pl.kernelpanic.dbmonster.generator.ForeignKeyGenerator;
import pl.kernelpanic.dbmonster.generator.KeyGenerator;
import pl.kernelpanic.dbmonster.generator.MaxKeyGenerator;
import pl.kernelpanic.dbmonster.generator.NullGenerator;
import pl.kernelpanic.dbmonster.generator.NumberGenerator;
import pl.kernelpanic.dbmonster.generator.StringGenerator;
import pl.kernelpanic.dbmonster.generator.StringKeyGenerator;
import pl.kernelpanic.dbmonster.schema.Column;
import pl.kernelpanic.dbmonster.schema.Key;
import pl.kernelpanic.dbmonster.schema.Schema;
import pl.kernelpanic.dbmonster.schema.SchemaException;
import pl.kernelpanic.dbmonster.schema.SchemaUtil;
import pl.kernelpanic.dbmonster.schema.Table;
import pl.kernelpanic.dbmonster.sql.ExtendedTypes;


public class SchemaGrabber {
    private Log log = LogFactory.getLog(SchemaGrabber.class);


    private ConnectionProvider connectionProvider = null;


    private Vector tables = null;


    private OutputStream output = System.out;


    private Properties properties = new Properties();


    private String dbSchema = null;


    private int numRows = 1000;


    public static void main(String[] args) throws Exception {
        SchemaGrabber sg = new SchemaGrabber();
        DBCPConnectionProvider dBCPConnectionProvider = new DBCPConnectionProvider();
        sg.setConnectionProvider(dBCPConnectionProvider);
        Schema schema = sg.grabSchema();
        OutputStream os = sg.getOutput();
        SchemaUtil.serializeSchema(new PrintWriter(os), schema);
    }


    public final void doTheJob() throws Exception {
        Schema schema = grabSchema();
        SchemaUtil.serializeSchema(new PrintWriter(this.output), schema);
    }


    public final OutputStream getOutput() {
        return this.output;
    }


    public final Schema grabSchema() throws Exception {
        this.dbSchema = this.properties.getProperty("dbmonster.jdbc.schema", null);
        if (this.dbSchema != null && "".equals(this.dbSchema)) {
            this.dbSchema = null;
        }
        String rows = this.properties.getProperty("dbmonster.rows", "1000");
        try {
            this.numRows = Integer.valueOf(rows).intValue();
        } catch (Exception e) {
        }

        Schema schema = new Schema();
        schema.setName("Change me!");
        if (this.tables == null) {
            this.tables = getTableNames();
        }
        Iterator it = this.tables.iterator();
        int count = this.tables.size();
        int current = 1;
        this.log.info("Grabbing schema from database. " + count + " tables to grab.");

        while (it.hasNext()) {
            String tableName = (String) it.next();
            Table t = grabTable(tableName);
            schema.addTable(t);
            this.log.info("Grabbing table " + tableName + ". " + (current * 100 / count) + "% done.");

            current++;
        }
        this.log.info("Grabbing schema from database complete.");
        return schema;
    }


    public final Table grabTable(String name) throws SQLException, SchemaException {
        Table t = new Table();
        String tableName = name;
        if (this.dbSchema != null) {
            tableName = this.dbSchema + "." + tableName;
        }
        t.setName(tableName);
        t.setRows(this.numRows);
        Transaction tx = null;
        try {
            tx = new Transaction(this.connectionProvider);
            Connection conn = tx.begin();
            DatabaseMetaData md = conn.getMetaData();


            Vector keyColumns = new Vector();
            ResultSet rs = md.getPrimaryKeys(null, this.dbSchema, name);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                keyColumns.add(columnName);
            }
            KeyGenerator generator = suggestKeyGenerator(name, keyColumns);
            if (generator != null) {
                Key key = new Key();
                key.setGenerator(generator);
                t.setKey(key);
            } else {

                keyColumns.clear();
            }


            HashMap foreignKeys = new HashMap();
            rs = md.getImportedKeys(null, null, name);
            while (rs.next()) {
                String fkColumn = rs.getString("FKCOLUMN_NAME");
                String pkColumn = rs.getString("PKCOLUMN_NAME");
                String pkTable = rs.getString("PKTABLE_NAME");


                foreignKeys.put(fkColumn, new PairBean(pkTable, pkColumn));
            }


            rs = md.getColumns(null, this.dbSchema, name, "%");
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                if (keyColumns.contains(columnName)) {
                    continue;
                }
                Column c = new Column();
                c.setName(columnName);
                c.setGenerator(suggestGenerator(name, columnName, foreignKeys));
                t.addColumn(c);
            }

        } catch (SQLException e) {
            tx.abort();
            this.log.fatal(e.getMessage());
            throw e;
        } finally {
            if (tx != null) {
                tx.close();
            }
        }
        return t;
    }


    public final Vector getTableNames() throws SQLException {
        Vector v = new Vector();
        Transaction tx = null;
        try {
            tx = new Transaction(this.connectionProvider);
            Connection conn = tx.begin();
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, this.dbSchema, "%", new String[]{"TABLE"});

            while (rs.next()) {
                String name = rs.getString("TABLE_NAME");
                v.add(name);
            }
            tx.commit();
        } catch (SQLException e) {
            this.log.fatal(e.getMessage());
            tx.abort();
            throw e;
        } finally {
            if (tx != null) {
                tx.close();
            }
        }
        return v;
    }


    public final ConnectionProvider getConnectionProvider() {
        return this.connectionProvider;
    }


    public final void setConnectionProvider(ConnectionProvider provider) {
        this.connectionProvider = provider;
    }


    public final Log getLog() {
        return this.log;
    }


    public final void setLog(Log logger) {
        this.log = logger;
    }


    public final void addTable(String name) throws SQLException {
        if (this.tables == null) {
            this.tables = new Vector();
        }
        String tableName = name;
        Transaction tx = null;
        try {
            tx = new Transaction(this.connectionProvider);
            Connection conn = tx.begin();
            DatabaseMetaData md = conn.getMetaData();
            if (md.storesLowerCaseIdentifiers()) {
                tableName = name.toLowerCase();
            } else if (md.storesUpperCaseIdentifiers()) {
                tableName = name.toUpperCase();
            }
            ResultSet rs = md.getTables(null, this.dbSchema, tableName, new String[]{"TABLE"});

            if (!rs.next()) {
                throw new SQLException("No such table <" + name + ">.");
            }
            tx.commit();
            this.tables.add(tableName);
        } catch (SQLException e) {
            this.log.fatal(e.getMessage());
            tx.abort();
            throw e;
        } finally {
            if (tx != null) {
                tx.close();
            }
        }
    }


    public final void setOutputFile(String file) throws SQLException, FileNotFoundException {
        File f = new File(file);
        this.output = new FileOutputStream(f);
    }


    public final void setProperties(Properties p) {
        this.properties = p;
    }


    private final KeyGenerator suggestKeyGenerator(String tableName, Vector columns) throws SQLException {
        StringKeyGenerator stringKeyGenerator = null;

        if (columns.size() != 1) {
            return stringKeyGenerator;
        }
        String columnName = (String) columns.get(0);
        Transaction tx = null;
        try {
            tx = new Transaction(this.connectionProvider);
            Connection conn = tx.begin();
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getColumns(null, null, tableName, columnName);
            rs.next();
            int dataType = rs.getInt("DATA_TYPE");
            if (isIntegerType(dataType)) {
                MaxKeyGenerator maxKeyGenerator = new MaxKeyGenerator();
                ((MaxKeyGenerator) maxKeyGenerator).setColumnName(columnName);
            } else if (isTextType(dataType)) {
                stringKeyGenerator = new StringKeyGenerator();
                ((StringKeyGenerator) stringKeyGenerator).setStartValue("0");
                ((StringKeyGenerator) stringKeyGenerator).setColumnName(columnName);
            } else if (this.log.isWarnEnabled()) {
                this.log.warn("Datatype " + dataType + " for " + columnName + " is unknown, no Key Generator in schema.xml");
            }


            tx.commit();
        } catch (SQLException e) {
            tx.abort();
            this.log.fatal(e.getMessage());
            throw e;
        } finally {
            if (tx != null) {
                tx.close();
            }
        }
        return stringKeyGenerator;
    }


    private final DataGenerator suggestGenerator(String tableName, String columnName, Map foreignKeys) throws SQLException {
        DateTimeGenerator dateTimeGenerator = new DateTimeGenerator();
        if (foreignKeys.containsKey(columnName)) {
            ForeignKeyGenerator foreignKeyGenerator = new ForeignKeyGenerator();
            PairBean bean = (PairBean) foreignKeys.get(columnName);
            ((ForeignKeyGenerator) foreignKeyGenerator).setTableName(bean.getKey());
            ((ForeignKeyGenerator) foreignKeyGenerator).setColumnName(bean.getValue());
            return foreignKeyGenerator;
        }
        Transaction tx = null;
        try {
            tx = new Transaction(this.connectionProvider);
            Connection conn = tx.begin();
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getColumns(null, null, tableName, columnName);
            rs.next();
            int dataType = rs.getInt("DATA_TYPE");
            String isNullable = rs.getString("IS_NULLABLE");
            int nulls = 0;
            if (isNullable == null) {
                isNullable = "NO";
            }
            if ("YES".equals(isNullable)) {
                nulls = 10;
            }
            int columnSize = rs.getInt("COLUMN_SIZE");
            int decimalDigits = rs.getInt("DECIMAL_DIGITS");
            if (isIntegerType(dataType)) {
                NumberGenerator numberGenerator = new NumberGenerator();
                NumberGenerator numGen = (NumberGenerator) numberGenerator;
                numGen.setNulls(nulls);
                if (dataType == -5) {
                    numGen.setReturnedType("long");
                } else if (dataType == 4) {
                    numGen.setReturnedType("integer");
                } else if (dataType == 5 || dataType == -6) {

                    numGen.setReturnedType("short");
                } else if (dataType == 3 || dataType == 2) {
                    if (decimalDigits > 0) {
                        numGen.setReturnedType("float");
                        numGen.setScale(decimalDigits);
                    } else {
                        numGen.setReturnedType("integer");
                    }
                }
            } else if (isBooleanType(dataType)) {
                BooleanGenerator booleanGenerator = new BooleanGenerator();
                ((BooleanGenerator) booleanGenerator).setNulls(nulls);
            } else if (isTextType(dataType)) {
                StringGenerator stringGenerator = new StringGenerator();
                ((StringGenerator) stringGenerator).setNulls(nulls);
                if (columnSize > 0) {
                    ((StringGenerator) stringGenerator).setMaxLength(columnSize);
                }
            } else if (isTimeType(dataType)) {
                dateTimeGenerator = new DateTimeGenerator();
                ((DateTimeGenerator) dateTimeGenerator).setNulls(nulls);
                if (dataType == 91) {
                    ((DateTimeGenerator) dateTimeGenerator).setReturnedType("date");
                } else if (dataType == 92) {
                    ((DateTimeGenerator) dateTimeGenerator).setReturnedType("time");
                } else if (dataType == 93) {
                    ((DateTimeGenerator) dateTimeGenerator).setReturnedType("timestamp");
                }

            } else if (this.log.isWarnEnabled()) {
                this.log.warn("Unknown datatype. No generator created.");
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tx != null) {
                tx.close();
            }
        }
        return dateTimeGenerator;
    }


    private final boolean isIntegerType(int type) {
        int tempType = ExtendedTypes.getInstance().getStandardAlias(this.properties.getProperty("dbmonster.jdbc.driver"), type);


        return (tempType == -5 || tempType == 4 || tempType == 5 || tempType == -6 || tempType == 3 || tempType == 2);
    }


    private final boolean isTextType(int type) {
        int tempType = ExtendedTypes.getInstance().getStandardAlias(this.properties.getProperty("dbmonster.jdbc.driver"), type);


        return (tempType == 1 || tempType == -1 || tempType == 12);
    }


    private final boolean isTimeType(int type) {
        int tempType = ExtendedTypes.getInstance().getStandardAlias(this.properties.getProperty("dbmonster.jdbc.driver"), type);


        return (tempType == 91 || tempType == 92 || tempType == 93);
    }


    private final boolean isBooleanType(int type) {
        int tempType = ExtendedTypes.getInstance().getStandardAlias(this.properties.getProperty("dbmonster.jdbc.driver"), type);


        return (tempType == -7 || tempType == 16);
    }
}