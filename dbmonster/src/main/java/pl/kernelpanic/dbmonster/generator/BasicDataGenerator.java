package pl.kernelpanic.dbmonster.generator;

import pl.kernelpanic.dbmonster.schema.Column;
import pl.kernelpanic.dbmonster.util.Converter;


public abstract class BasicDataGenerator
        implements DataGenerator {
    protected int nulls = 0;


    protected Column column = null;


    public void setColumn(Column col) {
        this.column = col;
    }


    public Column getColumn() {
        return this.column;
    }


    public int getNulls() {
        return this.nulls;
    }


    public void setNulls(int count) {
        this.nulls = Converter.checkNulls(count);
    }

    public abstract void reset();

    public abstract Object generate() throws Exception;
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\BasicDataGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */