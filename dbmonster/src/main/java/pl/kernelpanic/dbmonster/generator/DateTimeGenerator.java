package pl.kernelpanic.dbmonster.generator;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.TimeZone;

import pl.kernelpanic.dbmonster.DBMonsterContext;


public class DateTimeGenerator
        extends BasicDataGenerator
        implements Initializable {
    public static final int DATE = 0;
    public static final int TIME = 1;
    public static final int TIMESTAMP = 2;
    private BigDecimal minValue = new BigDecimal("0");


    private BigDecimal maxValue = new BigDecimal("1423453127");


    private int returnedType = 0;


    private Random random = null;


    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S Z");


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
        if (this.returnedType == 1)
            return new Time(retValue.toBigInteger().longValue());
        if (this.returnedType == 2) {
            return new Timestamp(retValue.toBigInteger().longValue());
        }
        return new Date(retValue.toBigInteger().longValue());
    }


    public final String getStartDate() {
        return DATE_FORMAT.format(new Date(this.minValue.longValue()));
    }


    public final void setStartDate(String start) throws Exception {
        this.minValue = new BigDecimal("" + dateToLong(start));
    }


    public final String getEndDate() {
        return DATE_FORMAT.format(new Date(this.maxValue.longValue()));
    }


    public final void setEndDate(String end) throws Exception {
        this.maxValue = new BigDecimal("" + dateToLong(end));
    }


    public final String getReturnedType() {
        if (this.returnedType == 1) {
            return "time";
        }
        if (this.returnedType == 2) {
            return "timestamp";
        }
        return "date";
    }


    public final void setReturnedType(String type) throws Exception {
        if ("time".equals(type)) {
            this.returnedType = 1;
        } else if ("timestamp".equals(type)) {
            this.returnedType = 2;
        } else {
            this.returnedType = 0;
        }
    }


    public final void reset() {
    }


    private final long dateToLong(String dateStr) throws Exception {
        String date = dateStr;
        if (date == null) {
            return 0L;
        }
        if (date.length() == 10) {
            date = date + " 00:00:00";
        }
        if (date.length() == 16) {
            date = date + ":00";
        }
        if (date.length() == 19) {
            date = date + ".000";
        }
        if (date.length() == 23) {
            date = date + " " + TimeZone.getDefault().getDisplayName();
        }


        java.util.Date d = DATE_FORMAT.parse(date);
        return d.getTime();
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\DateTimeGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */