package pl.kernelpanic.dbmonster.schema;

import org.apache.commons.collections.Predicate;


public class ColumnPredicate
        implements Predicate {
    public final boolean evaluate(Object object) {
        Column column = (Column) object;
        return !column.getDatabaseDefault();
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\schema\ColumnPredicate.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */