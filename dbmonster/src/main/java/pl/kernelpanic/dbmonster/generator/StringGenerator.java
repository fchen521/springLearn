package pl.kernelpanic.dbmonster.generator;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import pl.kernelpanic.dbmonster.DBMonsterContext;
import pl.kernelpanic.dbmonster.Dictionary;
import pl.kernelpanic.dbmonster.DictionaryManager;


public class StringGenerator
        extends BasicDataGenerator
        implements Initializable {


    private int minLength = 0;


    private int maxLength = 255;


    private boolean allowSpaces = true;


    private Map excludeChars = new HashMap();


    private Dictionary dictionary = null;


    private Random random = null;


    private DBMonsterContext context = null;


    public final void initialize(DBMonsterContext ctx) throws Exception {
        if (this.minLength > this.maxLength) {
            throw new Exception("minLength > maxLength is string generator!");
        }
        this.context = ctx;
        this.random = (Random) this.context.getProperty("pl.kernelpanic.dbmonster.RANDOM_KEY");
        DictionaryManager dm = (DictionaryManager) this.context.getProperty("pl.kernelpanic.dbmonster.DICTIONARY_MANAGER_KEY");


        URL url = StringGenerator.class.getClassLoader().getResource("dictionary.gz");
        this.dictionary = dm.getDictionary(url);
    }


    public final Object generate() {
        if (this.nulls != 0 && this.random.nextInt(101) <= this.nulls) {
            return null;
        }

        StringBuffer buff = new StringBuffer();
        int k = 0;
        int length = this.random.nextInt(this.maxLength - this.minLength + 1);
        length += this.minLength;

        String s = null;
        while (k < length) {
            s = (String) this.dictionary.getNextRandomItem();
            s = filter(s);
            buff.append(s);
            k += s.length();

            if (this.allowSpaces) {
                buff.append(" ");
                k++;
            }
        }

        buff.setLength(length);

        return buff.toString();
    }


    public final int getMinLength() {
        return this.minLength;
    }


    public final void setMinLength(int length) {
        this.minLength = length;
    }


    public final int getMaxLength() {
        return this.maxLength;
    }


    public final void setMaxLength(int length) {
        this.maxLength = length;
    }


    public final boolean getAllowSpaces() {
        return this.allowSpaces;
    }


    public final void setAllowSpaces(boolean spaces) {
        this.allowSpaces = spaces;
    }


    public final String getExcludeChars() {
        Iterator it = this.excludeChars.keySet().iterator();
        StringBuffer buff = new StringBuffer();
        while (it.hasNext()) {
            buff.append(it.next());
        }
        return buff.toString();
    }


    public final void setExcludeChars(String chars) {
        this.excludeChars.clear();
        if (chars == null) {
            return;
        }
        for (int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            this.excludeChars.put(new Character(c), null);
        }
    }


    public final void reset() {
    }


    private String filter(String str) {
        if (str == null) {
            return null;
        }
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isWhitespace(c)) {
                if (this.allowSpaces) {
                    buff.append(c);
                }
            } else if (!this.excludeChars.containsKey(new Character(c))) {
                buff.append(c);
            }
        }
        return buff.toString();
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\StringGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */