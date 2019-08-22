package pl.kernelpanic.dbmonster.generator;

import pl.kernelpanic.dbmonster.schema.Column;

public interface DataGenerator {
    void setColumn(Column paramColumn);

    Object generate() throws Exception;

    void reset();
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\DataGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */