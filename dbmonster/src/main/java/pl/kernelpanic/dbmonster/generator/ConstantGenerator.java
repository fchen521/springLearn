package pl.kernelpanic.dbmonster.generator;


public class ConstantGenerator
        extends BasicDataGenerator {
    private Object constant;

    public Object generate() {
        return this.constant;
    }


    public void reset() {
    }


    public String getConstant() {
        return (String) this.constant;
    }


    public void setConstant(String constant) {
        this.constant = constant;
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\ConstantGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */