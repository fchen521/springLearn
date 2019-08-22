package pl.kernelpanic.dbmonster.schema;

import java.util.List;

import org.apache.commons.logging.Log;
import pl.kernelpanic.dbmonster.DBMonsterContext;
import pl.kernelpanic.dbmonster.generator.Initializable;
import pl.kernelpanic.dbmonster.generator.KeyGenerator;


public class Key {
    private Table table = null;


    private KeyGenerator generator = null;


    private DBMonsterContext context = null;


    private boolean databaseDefault = false;


    public final KeyGenerator getGenerator() {
        return this.generator;
    }


    public final void setGenerator(KeyGenerator gen) {
        this.generator = gen;
        this.generator.setKey(this);
    }


    public final void initialize(DBMonsterContext ctx) throws Exception {
        this.context = ctx;

        Log log = (Log) this.context.getProperty("pl.kernelpanic.dbmonster.LOGGER_KEY");
        if (log.isDebugEnabled()) {
            log.debug("Initializing key for table <" + this.table.getName() + ">.");
        }


        if (this.generator instanceof Initializable) {
            ((Initializable) this.generator).initialize(ctx);
        }

        if (log.isDebugEnabled()) {
            log.debug("Initialization of key for table <" + this.table.getName() + "> finished.");
        }
    }


    public final List generate() throws Exception {
        return this.generator.generate();
    }


    public final void setTable(Table t) {
        this.table = t;
    }


    public final Table getTable() {
        return this.table;
    }


    public final boolean getDatabaseDefault() {
        return this.databaseDefault;
    }


    public final void setDatabaseDefault(boolean isDatabaseDefault) {
        this.databaseDefault = isDatabaseDefault;
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\schema\Key.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */