package pl.kernelpanic.dbmonster;

import java.util.HashMap;
import java.util.Map;


public class DBMonsterContext {
    private Map properties = new HashMap();


    public final Object getProperty(String key) {
        return this.properties.get(key);
    }


    public final void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\DBMonsterContext.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */