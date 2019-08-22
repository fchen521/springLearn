package pl.kernelpanic.dbmonster;


final class PairBean {
    private String key;
    private String value;

    public PairBean(String k, String v) {
        this.key = null;


        this.value = null;


        this.key = k;
        this.value = v;
    }


    public final String getKey() {
        return this.key;
    }


    public final void setKey(String k) {
        this.key = k;
    }


    public final String getValue() {
        return this.value;
    }


    public final void setValue(String v) {
        this.value = v;
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\PairBean.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */