package pl.kernelpanic.dbmonster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Dictionary {
    private String name = null;


    private Random random = null;


    private List items = new ArrayList();


    private int nextUniqueIndex = -1;


    public final Object getNextRandomItem() {
        return this.items.get(this.random.nextInt(this.items.size()));
    }


    public final Object getNextUniqueItem() throws Exception {
        setNextUnique();
        return this.items.get(this.nextUniqueIndex);
    }


    public final void reset() {
        this.nextUniqueIndex = -1;
    }


    public final String getName() {
        return this.name;
    }


    public final void setName(String s) {
        this.name = s;
    }


    public final void setRandom(Random r) {
        this.random = r;
    }


    public final void addItem(String item) {
        this.items.add(item);
    }


    private final void setNextUnique() throws Exception {
        if (this.items.size() > 0 && this.nextUniqueIndex < this.items.size() - 1) {
            this.nextUniqueIndex++;
        } else {
            throw new Exception("No more unique values in dictionary: " + this.name);
        }
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\Dictionary.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */