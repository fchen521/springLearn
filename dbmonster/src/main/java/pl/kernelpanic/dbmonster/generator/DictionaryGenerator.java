package pl.kernelpanic.dbmonster.generator;

import pl.kernelpanic.dbmonster.DBMonsterContext;
import pl.kernelpanic.dbmonster.Dictionary;
import pl.kernelpanic.dbmonster.DictionaryManager;


public class DictionaryGenerator
        extends BasicDataGenerator
        implements Initializable {
    private DBMonsterContext context = null;


    private String dictFile = null;


    private boolean unique = false;


    private Dictionary dictionary = null;


    private boolean firstUse = true;


    public final void initialize(DBMonsterContext ctx) throws Exception {
        this.context = ctx;
        DictionaryManager dm = (DictionaryManager) this.context.getProperty("pl.kernelpanic.dbmonster.DICTIONARY_MANAGER_KEY");


        String schemaPath = this.column.getTable().getSchema().getHome();
        this.dictionary = dm.getDictionary(schemaPath, this.dictFile);
    }


    public final Object generate() throws Exception {
        if (this.firstUse) {
            this.dictionary.reset();
            this.firstUse = false;
        }
        if (this.unique) {
            return this.dictionary.getNextUniqueItem();
        }
        return this.dictionary.getNextRandomItem();
    }


    public final void setDictFile(String name) {
        this.dictFile = name;
    }


    public final String getDictFile() {
        return this.dictFile;
    }


    public final void setUnique(boolean b) {
        this.unique = b;
    }


    public final boolean getUnique() {
        return this.unique;
    }


    public final void reset() {
        this.firstUse = true;
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\DictionaryGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */