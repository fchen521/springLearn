package pl.kernelpanic.dbmonster;


public class ProgressMonitorAdapter
        implements ProgressMonitor {
    protected int schemasCount = 0;
    protected int tablesCount = 0;
    protected int rowsCount = 0;

    protected String schemaName;

    protected String tableName;


    public void setUp() {
    }


    public void tearDown() {
    }


    public void reset() {
    }


    public void resetTables() {
    }

    public void resetRows() {
    }

    public void setSchemaCount(int count) {
        this.schemasCount = count;
    }


    public void setTableCount(int count) {
        this.tablesCount = count;
    }


    public void setRowsCount(int count) {
        this.rowsCount = count;
    }


    public void schemaComplete() {
    }


    public void tableComplete() {
    }


    public void rowComplete() {
    }


    public void setSchemaName(String name) {
        this.schemaName = name;
    }


    public void setTableName(String name) {
        this.tableName = name;
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\ProgressMonitorAdapter.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */