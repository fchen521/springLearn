package pl.kernelpanic.dbmonster.sql;

import java.util.HashMap;
import java.util.Map;


public class ExtendedTypes {
    private static final ExtendedTypes single = new ExtendedTypes();


    private Map jdbcDriver2typeMap = new HashMap();

    private ExtendedTypes() {
        Map typeMap = new HashMap();
        this.jdbcDriver2typeMap.put("net.sourceforge.jtds.jdbc.Driver", typeMap);


        typeMap.put(new Integer(-9), new Integer(12));
    }


    public static final ExtendedTypes getInstance() {
        return single;
    }


    public final boolean isExtended(String driverName, int type) {
        boolean retValue = false;


        Map typeMap = (Map) this.jdbcDriver2typeMap.get(driverName);
        if (typeMap != null) {
            Integer tempInt = (Integer) typeMap.get(new Integer(type));
            if (tempInt != null)
                retValue = true;
        }
        return retValue;
    }


    public final int getStandardAlias(String driverName, int nonStandardType) {
        int retValue = nonStandardType;
        Map typeMap = (Map) this.jdbcDriver2typeMap.get(driverName);
        if (typeMap != null) {
            Integer tempInt = (Integer) typeMap.get(new Integer(nonStandardType));
            if (tempInt != null)
                retValue = tempInt.intValue();
        }
        return retValue;
    }


    public final Map getTypeMap(String driverName) {
        return (Map) this.jdbcDriver2typeMap.get(driverName);
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\sql\ExtendedTypes.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */