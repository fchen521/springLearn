package pl.kernelpanic.dbmonster.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import pl.kernelpanic.dbmonster.DBMonsterContext;
import pl.kernelpanic.dbmonster.ProgressMonitor;


public class Schema
        implements Comparable {
    private String name = "default";


    private Map tables = new HashMap();


    private String home = ".";


    private DBMonsterContext context = null;


    private ProgressMonitor progressMonitor = null;


    public void addTable(Table table) throws SchemaException {
        if (table == null) {
            throw new SchemaException("No table.");
        }
        String tableName = table.getName();
        if (tableName == null) {
            throw new SchemaException("No table name.");
        }
        if (this.tables.containsKey(tableName)) {
            throw new SchemaException("Table <" + tableName + "> already exists.");
        }

        table.setSchema(this);
        this.tables.put(tableName, table);
    }


    public String getName() {
        return this.name;
    }


    public void setName(String s) {
        this.name = s;
    }


    public void initialize(DBMonsterContext ctx) throws Exception {
        this.context = ctx;

        Log log = (Log) this.context.getProperty("pl.kernelpanic.dbmonster.LOGGER_KEY");
        this.progressMonitor = (ProgressMonitor) this.context.getProperty("pl.kernelpanic.dbmonster.PROGRESS_MONITOR_KEY");


        if (log.isDebugEnabled()) {
            log.debug("Initializing schema <" + this.name + ">, home: <" + this.home + ">.");
        }

        if (this.progressMonitor != null) {
            this.progressMonitor.setTableCount(this.tables.size());
        }
        Iterator it = this.tables.values().iterator();
        while (it.hasNext()) {
            ((Table) it.next()).initialize(ctx);
        }

        if (log.isDebugEnabled()) {
            log.debug("Initialization of schema <" + this.name + "> finished.");
        }
    }


    public void generate() throws Exception {
        Log log = (Log) this.context.getProperty("pl.kernelpanic.dbmonster.LOGGER_KEY");
        if (log.isInfoEnabled()) {
            log.info("Generating schema <" + this.name + ">.");
        }
        Iterator it = this.tables.values().iterator();
        while (it.hasNext()) {
            ((Table) it.next()).generate();
        }
        if (log.isInfoEnabled()) {
            log.info("Generation of schema <" + this.name + "> finished.");
        }
    }


    public String getHome() {
        return this.home;
    }


    public void setHome(String dir) {
        this.home = dir;
    }


    public Table findTable(String tableName) {
        return (Table) this.tables.get(tableName);
    }


    public List getTables() {
        Collection coll = this.tables.values();
        List list = new ArrayList();
        Iterator it = coll.iterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }


    public void removeTable(Table t) throws SchemaException {
        Table table = (Table) this.tables.remove(t.getName());
        table.setSchema(null);
    }


    public int compareTo(Object o) {
        Schema s = (Schema) o;
        return getName().compareTo(s.getName());
    }


    public void reset() {
        Iterator it = getTables().iterator();
        while (it.hasNext()) {
            Table t = (Table) it.next();
            t.reset();
        }
    }
}