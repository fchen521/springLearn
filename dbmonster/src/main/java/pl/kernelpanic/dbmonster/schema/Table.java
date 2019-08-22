package pl.kernelpanic.dbmonster.schema;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.logging.Log;
import pl.kernelpanic.dbmonster.DBMonsterContext;
import pl.kernelpanic.dbmonster.ProgressMonitor;
import pl.kernelpanic.dbmonster.connection.ConnectionProvider;
import pl.kernelpanic.dbmonster.connection.Transaction;


public class Table
        implements Comparable {
    private String name = null;


    private Schema schema = null;


    private Key key = null;


    private List columns = new ArrayList();


    private DBMonsterContext context = null;


    private ProgressMonitor progressMonitor = null;


    private int rows = 0;


    private String query = null;


    private long maxTries = 1000L;


    private boolean monitorSet = false;


    private Map columnTypes = new HashMap();


    public final String getName() {
        return this.name;
    }


    public final void setName(String s) {
        this.name = s;
    }


    public final Schema getSchema() {
        return this.schema;
    }


    public final void setSchema(Schema s) {
        this.schema = s;
    }


    private boolean generated = false;


    private ConnectionProvider connectionProvider;


    public final void addColumn(Column column) throws SchemaException {
        if (column == null) {
            throw new SchemaException("Column is null!");
        }
        String columnName = column.getName();
        if (containsColumn(columnName)) {
            throw new SchemaException("Column <" + columnName + "> already exists.");
        }
        column.setTable(this);
        this.columns.add(column);
    }


    public final void setKey(Key k) {
        this.key = k;
        if (this.key != null) {
            this.key.setTable(this);
        }
    }


    public final Key getKey() {
        return this.key;
    }


    public final void initialize(DBMonsterContext ctx) throws Exception {
        this.context = ctx;

        Log log = (Log) this.context.getProperty("pl.kernelpanic.dbmonster.LOGGER_KEY");


        this.connectionProvider = (ConnectionProvider) this.context.getProperty("pl.kernelpanic.dbmonster.CONNECTION_PROVIDER_KEY");

        Connection conn = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Getting column types.");
            }

            conn = this.connectionProvider.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            String schemaName = (String) ctx.getProperty("dbmonster.jdbc.schema");
            ResultSet rs = metaData.getColumns(null, schemaName, this.name, null);

            String columnName = null;
            int columnType = 0;
            while (rs.next()) {
                columnName = rs.getString("COLUMN_NAME");
                columnType = rs.getInt("DATA_TYPE");
                this.columnTypes.put(columnName, new Integer(columnType));
                if (log.isDebugEnabled()) {
                    log.debug("Column: " + columnName + ", SQL type: " + columnType);
                }
            }
        } catch (Exception e) {
            throw new Exception("Couldn't get column types from database. Buggy JDBC driver?");
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        String lng = (String) this.context.getProperty("dbmonster.max-tries");
        try {
            this.maxTries = Long.parseLong(lng);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Unable to parse dbmonster.max-tries. Using default value of 1000.");
            }
        }


        this.progressMonitor = (ProgressMonitor) this.context.getProperty("pl.kernelpanic.dbmonster.PROGRESS_MONITOR_KEY");


        if (log.isDebugEnabled()) {
            log.debug("Initializing table <" + this.name + ">.");
        }

        StringBuffer sql = new StringBuffer("INSERT INTO ");
        sql.append(this.name);
        sql.append(" (");

        int count = 0;
        if (this.key != null && !this.key.getDatabaseDefault()) {
            this.key.initialize(ctx);
            List cols = this.key.getGenerator().getColumns();
            for (int i = 0; i < cols.size(); i++) {
                Column column = (Column) cols.get(i);
                if (i != 0) {
                    sql.append(", ");
                }
                sql.append(column.getName());
                count++;
            }
        }

        Iterator it = columnIterator();
        if (it.hasNext() &&
                this.key != null && !this.key.getDatabaseDefault()) {
            sql.append(", ");
        }


        boolean first = true;
        while (it.hasNext()) {
            Column column = (Column) it.next();
            column.initialize(ctx);
            if (!first) {
                sql.append(", ");
            }
            sql.append(column.getName());
            first = false;
            count++;
        }

        sql.append(") VALUES (");
        while (--count >= 0) {
            sql.append("?");
            if (count != 0) {
                sql.append(", ");
            }
        }
        sql.append(")");

        this.query = sql.toString();

        if (log.isDebugEnabled()) {
            log.debug("Query: " + this.query);
            log.debug("Initialization of table <" + this.name + "> finished.");
        }
    }


    public final void generate() throws Exception {
        int number = (Integer.parseInt((String)context.getProperty("dbmonster.max-number"))) ;

        Log log = (Log) this.context.getProperty("pl.kernelpanic.dbmonster.LOGGER_KEY");

        if (!this.generated) {

            if (log.isInfoEnabled()) {
                log.info("Generating table <" + this.name + ">.");
            }

            int counter = 0;
            List keyColumns = null;
            Transaction tx = null;
            try {
                tx = new Transaction(this.connectionProvider);
                tx.begin();
                PreparedStatement pstmt = tx.prepareStatement(this.query);
                int currentTry = 0;
                while (counter < this.rows) {
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    //pstmt.clearParameters();
                    if (this.key != null && !this.key.getDatabaseDefault()) {
                        if (log.isDebugEnabled()) {
                            log.debug("Generating key: " + this.key);
                        }
                        keyColumns = this.key.generate();
                    }

                    Iterator it = columnIterator();
                    while (it.hasNext()) {
                        ((Column) it.next()).generate();
                    }

                    if (!this.monitorSet) {
                        if (this.progressMonitor != null) {
                            this.progressMonitor.setTableName(this.name);
                        }
                        if (this.progressMonitor != null) {
                            this.progressMonitor.setRowsCount(this.rows);
                        }
                        this.monitorSet = true;
                    }

                    int paramCount = 1;
                    if (this.key != null && !this.key.getDatabaseDefault()) {
                        it = keyColumns.iterator();
                        while (it.hasNext()) {
                            Column column = (Column) it.next();
                            if (log.isDebugEnabled()) {
                                log.debug("Key column parameter " + paramCount + " is " + column.getValue() + ".");
                            }

                            Integer columnType = (Integer) this.columnTypes.get(column.getName());
                            if (columnType != null) {
                                pstmt.setObject(paramCount++, column.getValue(), columnType.intValue());
                                continue;
                            }
                            pstmt.setObject(paramCount++, column.getValue());
                        }
                    }


                    it = columnIterator();
                    while (it.hasNext()) {
                        Column column = (Column) it.next();
                        if (log.isDebugEnabled()) {
                            log.debug("Column parameter " + paramCount + " is " + column.getValue() + ".");
                        }

                        Integer columnType = (Integer) this.columnTypes.get(column.getName());
                        if (columnType != null) {
                            pstmt.setObject(paramCount++, column.getValue(), columnType.intValue());
                            continue;
                        }
                        pstmt.setObject(paramCount++, column.getValue());
                    }

                    pstmt.addBatch();

                    try {
                        //批量执行
                       /* tx.executeBatch();*/
                       if (number >=1){
                           counter++;
                           if (counter % number == 0){
                               log.debug("执行批量操作，批量数据为："+number);
                               tx.executeBatch();
                               tx.commit();
                           }
                       }else {
                           log.debug("单条执行"+number);
                           tx.execute();
                           counter++;
                       }

                        if (this.progressMonitor != null) {
                            this.progressMonitor.rowComplete();
                        }
                    } catch (Exception e) {
                        if (log.isDebugEnabled()) {
                            log.debug("Execution failed for reason: " + e.getMessage());
                        }

                        currentTry++;

                        if (currentTry > this.maxTries) {
                            throw new Exception("Max tries exceeded! Quiting.");
                        }
                    }
                    resetColumns();
                }
                if(number>=1){
                    int[] ints = pstmt.executeBatch();
                    log.debug("执行批量操作，批量数据为："+ints.length);
                }

                tx.commit();
            } catch (Exception e) {
                tx.abort();
                throw e;
            } finally {
                tx.close();
            }
            this.generated = true;
            if (this.progressMonitor != null) {
                this.progressMonitor.tableComplete();
            }
            if (log.isInfoEnabled()) {
                log.info("Generation of table <" + this.name + "> finished.");
            }
        }
    }


    public final void resetColumns() {
        Iterator it = columnIterator();
        while (it.hasNext()) {
            ((Column) it.next()).reset();
        }
    }


    public final void reset() {
        resetColumns();
        for (int i = 0; i < this.columns.size(); i++) {
            Column c = (Column) this.columns.get(i);
            c.getGenerator().reset();
        }
        this.generated = false;
        this.monitorSet = false;
    }


    public final int getRows() {
        return this.rows;
    }


    public final void setRows(int i) {
        this.rows = i;
    }


    public final boolean isGenerated() {
        return this.generated;
    }


    public final Iterator columnIterator() {
        return new FilterIterator(this.columns.iterator(), new ColumnPredicate());
    }


    public final List getColumns() {
        return this.columns;
    }


    public final int compareTo(Object o) {
        Table t = (Table) o;
        return getName().compareTo(t.getName());
    }


    public final void removeColumn(Column column) throws SchemaException {
        for (int i = 0; i < this.columns.size(); i++) {
            Column c = (Column) this.columns.get(i);
            if (c == column) {
                this.columns.remove(i);
                break;
            }
        }
    }

    public boolean containsColumn(String name) {
        for (int i = 0; i < this.columns.size(); i++) {
            Column column = (Column) this.columns.get(i);
            if (name.equals(column.getName())) {
                return true;
            }
        }
        return false;
    }

    public Column findColumn(String name) {
        Column c = null;
        for (int i = 0; i < this.columns.size(); i++) {
            c = (Column) this.columns.get(i);
            if (c.getName().equals(name)) {
                return c;
            }
            c = null;
        }
        return c;
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\schema\Table.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */