package pl.kernelpanic.dbmonster.generator;

import java.util.List;

import pl.kernelpanic.dbmonster.schema.Key;

public interface KeyGenerator {
    void setKey(Key paramKey);

    Key getKey();

    List getColumns();

    List generate();
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\KeyGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */