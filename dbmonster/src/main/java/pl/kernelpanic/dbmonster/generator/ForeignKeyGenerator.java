package pl.kernelpanic.dbmonster.generator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import pl.kernelpanic.dbmonster.DBMonsterContext;
import pl.kernelpanic.dbmonster.connection.ConnectionProvider;
import pl.kernelpanic.dbmonster.connection.Transaction;
import pl.kernelpanic.dbmonster.schema.Column;
import pl.kernelpanic.dbmonster.schema.Key;
import pl.kernelpanic.dbmonster.schema.Schema;
import pl.kernelpanic.dbmonster.schema.SchemaException;
import pl.kernelpanic.dbmonster.schema.Table;


public class ForeignKeyGenerator
        extends BasicDataGenerator
        implements Initializable {
    private String columnName = null;


    private String tableName = null;


    private boolean fastMode = false;


    private List items = null;


    private DBMonsterContext context = null;


    private ConnectionProvider connProv = null;


    private Log log = null;


    private Random random = null;


    private String sql = null;


    private boolean dependencyGenerated = false;


    private boolean initialized = false;


    public final void initialize(DBMonsterContext ctx) throws Exception {
        if (this.columnName == null || this.tableName == null) {
            throw new Exception("No columnName or tableName property set!");
        }

        this.context = ctx;
        this.connProv = (ConnectionProvider) this.context.getProperty("pl.kernelpanic.dbmonster.CONNECTION_PROVIDER_KEY");

        this.log = (Log) this.context.getProperty("pl.kernelpanic.dbmonster.LOGGER_KEY");
        this.random = (Random) this.context.getProperty("pl.kernelpanic.dbmonster.RANDOM_KEY");

        StringBuffer buff = new StringBuffer();
        buff.append("SELECT ");
        buff.append(this.columnName);
        buff.append(" FROM ");
        buff.append(this.tableName);
        this.sql = buff.toString();
    }


    public final Object generate() throws Exception {
        Schema schema = this.column.getTable().getSchema();
        Table table = schema.findTable(this.tableName);
        if (!this.dependencyGenerated) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("Ensure that table <" + this.tableName + "> is already generated.");
            }

            if (table == null) {
                throw new SchemaException("No table <" + this.tableName + "> in this schema!");
            }

            if (!table.getName().equals(getColumn().getTable().getName())) {
                table.generate();
            } else {
                this.fastMode = false;
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Yup! The table <" + this.tableName + "> is already generated.");
            }

            this.dependencyGenerated = true;
        }

        if (this.fastMode) {
            if (!this.initialized) {
                this.items = new ArrayList();
                Transaction tx = null;
                try {
                    tx = new Transaction(this.connProv);
                    tx.begin();
                    ResultSet rs = tx.executeQuery(this.sql);
                    while (rs.next()) {
                        this.items.add(rs.getObject(1));
                    }
                    tx.commit();
                } catch (Exception e) {
                    tx.abort();
                    if (this.log.isDebugEnabled()) {
                        this.log.debug(e.getMessage());
                    }
                    throw e;
                } finally {
                    tx.close();
                }
                this.initialized = true;
            }

            if (this.items.size() == 0) {
                throw new Exception("No items in foreign key.");
            }
            return this.items.get(this.random.nextInt(this.items.size()));
        }

        Transaction tx = null;
        int rowsCount = 0;
        try {
            tx = new Transaction(this.connProv);
            tx.begin();
            ResultSet rs = tx.executeQuery("SELECT count(*) FROM " + this.tableName);
            rs.next();
            rowsCount = rs.getInt(1);
            tx.commit();

            if (rowsCount == 0) {
                if (table.getName().equals(this.tableName)) {

                    Column referencedColumn = null;

                    Key key = table.getKey();
                    if (key != null) {
                        List columns = key.getGenerator().getColumns();
                        for (int i = 0; i < columns.size(); i++) {
                            referencedColumn = (Column) columns.get(i);
                            if (referencedColumn.getName().equals(this.columnName)) {
                                break;
                            }
                            referencedColumn = null;
                        }
                    }
                    if (referencedColumn == null) {

                        Iterator it = table.columnIterator();
                        while (it.hasNext()) {
                            Column column = (Column) it.next();
                            if (column.getName().equals(this.columnName)) {
                                column.generate();
                            }
                        }
                    }

                    return referencedColumn.getValue();
                }
                throw new Exception("There are no rows in table " + this.tableName);
            }

            rs = tx.executeQuery(this.sql);
            int rowNum = this.random.nextInt(rowsCount);
            int counter = 0;
            while (counter <= rowNum) {
                rs.next();
                counter++;
            }
            return rs.getObject(1);
        } catch (Exception e) {
            tx.abort();
            if (this.log.isDebugEnabled()) {
                this.log.debug(e.getMessage());
            }
            throw e;
        } finally {
            tx.close();
        }
    }


    public final String getColumnName() {
        return this.columnName;
    }


    public final void setColumnName(String name) {
        this.columnName = name;
    }


    public final String getTableName() {
        return this.tableName;
    }


    public final void setTableName(String name) {
        this.tableName = name;
    }


    public final boolean getFastMode() {
        return this.fastMode;
    }


    public final void setFastMode(boolean mode) {
        this.fastMode = mode;
    }


    public final void reset() {
        this.initialized = false;
        this.dependencyGenerated = false;
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\ForeignKeyGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */