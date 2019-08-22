package pl.kernelpanic.dbmonster.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.kernelpanic.dbmonster.DBMonsterContext;
import pl.kernelpanic.dbmonster.generator.DataGenerator;
import pl.kernelpanic.dbmonster.generator.Initializable;


public class Column
        implements Comparable {
    private String name = null;


    private Table table = null;


    private Object value = null;


    private DataGenerator generator = null;


    private List dependencies = new ArrayList();


    private boolean databaseDefault = false;


    private boolean generated = false;


    private int targetType = 12;


    public final String getName() {
        return this.name;
    }


    public final void setName(String s) {
        this.name = s;
    }


    public final DataGenerator getGenerator() {
        return this.generator;
    }


    public final void setGenerator(DataGenerator gen) {
        this.generator = gen;
        this.generator.setColumn(this);
    }


    public final void initialize(DBMonsterContext ctx) throws Exception {
        if (this.generator instanceof Initializable) {
            ((Initializable) this.generator).initialize(ctx);
        }
    }


    public final void generate() throws Exception {
        if (!this.generated) {
            Iterator it = this.dependencies.iterator();
            while (it.hasNext()) {
                String tableName = (String) it.next();
                if (!tableName.equals(this.table.getName())) {
                    Schema schema = this.table.getSchema();
                    Table t = schema.findTable(tableName);
                    t.generate();
                }
            }
            this.value = this.generator.generate();
            this.generated = true;
        }
    }


    public final void reset() {
        this.value = null;
        this.generated = false;
    }


    public final boolean getDatabaseDefault() {
        return this.databaseDefault;
    }


    public final void setDatabaseDefault(boolean isDatabaseDefault) {
        this.databaseDefault = isDatabaseDefault;
    }


    public final void setValue(Object o) {
        this.value = o;
    }


    public final Object getValue() {
        return this.value;
    }


    public final void setTable(Table t) {
        this.table = t;
    }


    public final Table getTable() {
        return this.table;
    }


    public final int compareTo(Object o) {
        Column s = (Column) o;
        return this.name.compareTo(s.name);
    }


    public int getTargetType() {
        return this.targetType;
    }


    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\schema\Column.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */