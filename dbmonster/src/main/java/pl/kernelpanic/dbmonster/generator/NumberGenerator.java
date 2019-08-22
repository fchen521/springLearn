package pl.kernelpanic.dbmonster.generator;

import java.math.BigDecimal;
import java.util.Random;

import pl.kernelpanic.dbmonster.DBMonsterContext;


public class NumberGenerator
        extends BasicDataGenerator
        implements Initializable {
    public static final int SHORT = 0;
    public static final int INTEGER = 1;
    public static final int LONG = 2;
    public static final int FLOAT = 3;
    public static final int DOUBLE = 4;
    public static final int NUMERIC = 5;
    private BigDecimal minValue = new BigDecimal("0");


    private BigDecimal maxValue = new BigDecimal("127");


    private int scale = 0;


    private int returnedType = 0;


    private Random random = null;


    public final void initialize(DBMonsterContext ctx) throws Exception {
        this.random = (Random) ctx.getProperty("pl.kernelpanic.dbmonster.RANDOM_KEY");
        if (this.minValue.compareTo(this.maxValue) > 0) {
            throw new Exception("MinValue < maxValue");
        }
    }


    public final Object generate() {
        if (this.nulls != 0 && this.random.nextInt(101) <= this.nulls) {
            return null;
        }

        BigDecimal retValue = null;
        BigDecimal length = this.maxValue.subtract(this.minValue);
        BigDecimal factor = new BigDecimal(this.random.nextDouble());
        retValue = length.multiply(factor).add(this.minValue);
        if (this.returnedType == 0) {
            return new Short((short) retValue.toBigInteger().intValue());
        }
        if (this.returnedType == 1) {
            return new Integer(retValue.toBigInteger().intValue());
        }
        if (this.returnedType == 2) {
            return new Long(retValue.toBigInteger().longValue());
        }
        if (this.returnedType == 3) {
            return new Float(retValue.floatValue());
        }
        if (this.returnedType == 4) {
            return new Double(retValue.doubleValue());
        }
        return retValue.setScale(this.scale, 6);
    }


    public final String getMinValue() {
        return this.minValue.toString();
    }


    public final void setMinValue(String minVal) {
        this.minValue = new BigDecimal(minVal);
    }


    public final String getMaxValue() {
        return this.maxValue.toString();
    }


    public final void setMaxValue(String maxVal) {
        this.maxValue = new BigDecimal(maxVal);
    }


    public final int getScale() {
        return this.scale;
    }


    public final void setScale(int s) {
        this.scale = s;
    }


    public final String getReturnedType() {
        if (this.returnedType == 1) {
            return "integer";
        }
        if (this.returnedType == 2) {
            return "long";
        }
        if (this.returnedType == 3) {
            return "float";
        }
        if (this.returnedType == 4) {
            return "double";
        }
        if (this.returnedType == 5) {
            return "numeric";
        }
        return "short";
    }


    public final void setReturnedType(String type) {
        if ("integer".equals(type)) {
            this.returnedType = 1;
        } else if ("long".equals(type)) {
            this.returnedType = 2;
        } else if ("float".equals(type)) {
            this.returnedType = 3;
        } else if ("double".equals(type)) {
            this.returnedType = 4;
        } else if ("numeric".equals(type)) {
            this.returnedType = 5;
        } else {
            this.returnedType = 0;
        }
    }

    public void reset() {
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\NumberGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */