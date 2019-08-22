package pl.kernelpanic.dbmonster;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.kernelpanic.dbmonster.connection.ConnectionProvider;
import pl.kernelpanic.dbmonster.connection.Transaction;
import pl.kernelpanic.dbmonster.schema.Schema;
import pl.kernelpanic.dbmonster.util.Converter;
import pl.kernelpanic.dbmonster.util.ScriptReaderIterator;


public class DBMonster {
    public static final String VERSION = "1.0.3";
    public static final String LOGGER_KEY = "pl.kernelpanic.dbmonster.LOGGER_KEY";
    public static final String CONNECTION_PROVIDER_KEY = "pl.kernelpanic.dbmonster.CONNECTION_PROVIDER_KEY";
    public static final String DICTIONARY_MANAGER_KEY = "pl.kernelpanic.dbmonster.DICTIONARY_MANAGER_KEY";
    public static final String RANDOM_KEY = "pl.kernelpanic.dbmonster.RANDOM_KEY";
    public static final String PROGRESS_MONITOR_KEY = "pl.kernelpanic.dbmonster.PROGRESS_MONITOR_KEY";
    private ConnectionProvider connProvider = null;


    private ArrayList schemaList = new ArrayList();


    private Log logger = LogFactory.getLog(DBMonster.class);


    private DBMonsterContext context = new DBMonsterContext();


    private ProgressMonitor progressMonitor = null;


    private File preScript;


    private File postScript;


    private Random random = new Random();


    public ConnectionProvider getConnectionProvider() {
        return this.connProvider;
    }


    public void setConnectionProvider(ConnectionProvider cp) {
        this.connProvider = cp;
    }


    public void addSchema(Schema schema) throws Exception {
        if (schemaExists(schema)) {
            throw new Exception("Schema named <" + schema.getName() + "> already exists.");
        }

        this.schemaList.add(schema);
    }


    public void setLogger(Log log) {
        this.logger = log;
    }


    public Log getLogger() {
        return this.logger;
    }


    public void setProperties(Properties props) {
        Enumeration enumeration =props.propertyNames();
        while ( enumeration.hasMoreElements()){
            String key = (String) enumeration.nextElement();
            this.context.setProperty(key, props.get(key));
        }
    }


    public void doTheJob() throws Exception {
        long t0 = System.currentTimeMillis();
        this.context.setProperty("pl.kernelpanic.dbmonster.LOGGER_KEY", this.logger);
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Let's feed this hungry database.");
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Checking the connection provider.");
        }
        if (this.connProvider == null) {
            throw new Exception("No connection provider.");
        }
        this.connProvider.testConnection();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Connection provider is OK.");
        }
        this.context.setProperty("pl.kernelpanic.dbmonster.CONNECTION_PROVIDER_KEY", this.connProvider);
        this.context.setProperty("pl.kernelpanic.dbmonster.RANDOM_KEY", this.random);
        this.context.setProperty("pl.kernelpanic.dbmonster.PROGRESS_MONITOR_KEY", this.progressMonitor);

        DictionaryManager dm = new DictionaryManager();
        dm.setRandom(this.random);
        this.context.setProperty("pl.kernelpanic.dbmonster.DICTIONARY_MANAGER_KEY", dm);

        if (this.preScript != null) {
            executeScript(this.connProvider, this.preScript);
        }
        Iterator it = this.schemaList.iterator();
        try {
            if (this.progressMonitor != null) {
                this.progressMonitor.setUp();
                this.progressMonitor.setSchemaCount(this.schemaList.size());
            }
            while (it.hasNext()) {
                Schema schema = (Schema) it.next();
                if (this.progressMonitor != null) {
                    this.progressMonitor.setSchemaName(schema.getName());
                }
                schema.initialize(this.context);
                schema.generate();
                if (this.progressMonitor != null) {
                    this.progressMonitor.schemaComplete();
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (this.progressMonitor != null) {
                this.progressMonitor.tearDown();
            }
        }
        if (this.postScript != null) {
            executeScript(this.connProvider, this.postScript);
        }

        t0 = System.currentTimeMillis() - t0;
        this.logger.info("Finished in " + Converter.formatTime(t0));
    }


    public static final String getVersion() {
        return "1.0.3";
    }


    private boolean schemaExists(Schema schema) {
        for (int i = 0; i < this.schemaList.size(); i++) {
            if (schema.compareTo(this.schemaList.get(i)) == 0) {
                return true;
            }
        }
        return false;
    }


    public ProgressMonitor getProgressMonitor() {
        return this.progressMonitor;
    }


    public void setProgressMonitor(ProgressMonitor monitor) {
        this.progressMonitor = monitor;
    }


    public void setPostScript(File postScript) {
        this.postScript = postScript;
    }


    public void setPreScript(File preScript) {
        this.preScript = preScript;
    }


    private void executeScript(ConnectionProvider cp, File scriptFile) throws Exception {
        ScriptReaderIterator it = new ScriptReaderIterator(scriptFile);
        Transaction tx = new Transaction(cp);
        try {
            tx.begin();
            while (it.hasNext()) {
                String query = (String) it.next();
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Executing query: " + query);
                }
                tx.prepareStatement(query);
                tx.execute();
            }
            tx.commit();
        } catch (Exception e) {
            tx.abort();
            throw e;
        } finally {
            tx.close();
        }
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\DBMonster.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */