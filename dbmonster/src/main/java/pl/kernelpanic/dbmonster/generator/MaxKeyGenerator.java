package pl.kernelpanic.dbmonster.generator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import pl.kernelpanic.dbmonster.DBMonsterContext;
import pl.kernelpanic.dbmonster.connection.ConnectionProvider;
import pl.kernelpanic.dbmonster.connection.Transaction;
import pl.kernelpanic.dbmonster.schema.Column;
import pl.kernelpanic.dbmonster.schema.Key;


public class MaxKeyGenerator
        implements KeyGenerator, Initializable {
    private Key key = null;


    private List columns = new ArrayList();


    private DBMonsterContext context = null;


    private long nextId = 0L;


    public void setKey(Key k) {
        this.key = k;
    }


    public Key getKey() {
        return this.key;
    }


    public List getColumns() {
        return this.columns;
    }


    public List generate() {
        Column column = (Column) this.columns.get(0);
        column.setValue(new Long(this.nextId++));
        return this.columns;
    }


    public void initialize(DBMonsterContext ctx) throws Exception {
        this.context = ctx;

        Log log = (Log) this.context.getProperty("pl.kernelpanic.dbmonster.LOGGER_KEY");
        if (log.isDebugEnabled()) {
            log.debug("Initializing generator <" + getClass().getName() + ">.");
        }

        ConnectionProvider connProv = (ConnectionProvider) this.context.getProperty("pl.kernelpanic.dbmonster.CONNECTION_PROVIDER_KEY");


        Transaction tx = new Transaction(connProv);
        try {
            tx.begin();
            Column column = (Column) this.columns.get(0);
            String sql = "SELECT max(" + column.getName() + ") FROM " + this.key.getTable().getName();


            if (log.isDebugEnabled()) {
                log.debug(sql);
            }

            ResultSet rs = tx.executeQuery(sql);
            if (rs.next()) {
                this.nextId = rs.getLong(1);
                if (log.isDebugEnabled()) {
                    log.debug("Key generator initial value is: " + this.nextId);
                }
                this.nextId++;
            }
            tx.commit();
        } catch (Exception e) {
            if (log.isFatalEnabled()) {
                log.fatal(e.getMessage());
            }
            tx.abort();
            throw e;
        } finally {
            tx.close();
        }

        if (log.isDebugEnabled()) {
            log.debug("Initialization of generator <" + getClass().getName() + "> finished.");
        }
    }


    public final String getColumnName() {
        if (this.columns.size() == 1) {
            Column column = (Column) this.columns.get(0);
            if (column != null) {
                return column.getName();
            }
        }
        return null;
    }


    public final void setColumnName(String name) {
        Column column = new Column();
        column.setName(name);
        this.columns.clear();
        this.columns.add(column);
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\MaxKeyGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */