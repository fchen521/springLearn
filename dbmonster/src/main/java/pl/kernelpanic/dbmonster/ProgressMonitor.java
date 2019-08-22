package pl.kernelpanic.dbmonster;

public interface ProgressMonitor {
    void setUp();

    void tearDown();

    void reset();

    void resetTables();

    void resetRows();

    void setSchemaCount(int paramInt);

    void setTableCount(int paramInt);

    void setRowsCount(int paramInt);

    void schemaComplete();

    void tableComplete();

    void rowComplete();

    void setSchemaName(String paramString);

    void setTableName(String paramString);
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\ProgressMonitor.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */