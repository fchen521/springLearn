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


public class StringKeyGenerator
        implements KeyGenerator, Initializable {
    public static final String DEFAULT_VALUE = "aaaaaaaa";
    private Key key = null;


    private List columns = new ArrayList();


    private String startValue = "0";


    private DBMonsterContext context = null;


    private String nextValue = "aaaaaaaa";


    public static final char[] CHARS = new char[26];

    static {
        for (int i = 97; i <= 122; i++) {
            CHARS[i - 97] = (char) i;
        }
    }


    public final void setKey(Key k) {
        this.key = k;
    }


    public Key getKey() {
        return this.key;
    }


    public final List getColumns() {
        return this.columns;
    }


    public final List generate() {
        Column column = (Column) this.columns.get(0);
        if (this.nextValue == null) {
            try {
                throw new Exception("No more values available (generator: " + getClass().getName() + ", table: " + this.key.getTable().getName() + ").");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        column.setValue(this.nextValue);
        this.nextValue = incrementString(this.nextValue);
        return this.columns;
    }


    public final void initialize(DBMonsterContext ctx) throws Exception {
        this.context = ctx;

        Log log = (Log) this.context.getProperty("pl.kernelpanic.dbmonster.LOGGER_KEY");
        if (log.isDebugEnabled()) {
            log.debug("Initializing generator <" + getClass().getName() + ">.");
        }

        if ("0".equals(this.startValue)) {
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
                    this.nextValue = rs.getString(1);
                    if (rs.wasNull() || "".equals(this.nextValue)) {
                        this.nextValue = "aaaaaaaa";
                    }
                    this.nextValue = incrementString(this.nextValue);
                } else {
                    this.nextValue = "aaaaaaaa";
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
        } else {
            this.nextValue = this.startValue;
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
        this.columns = new ArrayList();
        this.columns.add(column);
    }


    private final String incrementString(String s) {
        StringBuffer in = new StringBuffer(s);
        int pos = in.length() - 1;
        while (pos >= 0) {
            char c = in.charAt(pos);
            if (c != 'z') {
                in.setCharAt(pos, CHARS[c - 'a' + '\001']);
                break;
            }
            in.setCharAt(pos, 'a');

            pos--;
        }
        if (pos < 0) {
            return null;
        }
        return in.toString();
    }


    public final String getStartValue() {
        return this.startValue;
    }


    public final void setStartValue(String sv) {
        this.startValue = sv;
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\StringKeyGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */