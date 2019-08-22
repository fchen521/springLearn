package pl.kernelpanic.dbmonster.util;


public final class Converter {
    public static final String formatTime(long ms) {
        if (ms < 0L) {
            return "Faster than light!";
        }
        long millis = ms;
        long hours = millis / 3600000L;
        StringBuffer time = new StringBuffer();
        if (hours != 0L) {
            time.append(hours);
            time.append(" h.");
            millis -= hours * 3600000L;
        }

        long minutes = millis / 60000L;
        if (minutes != 0L) {
            if (time.length() > 0) {
                time.append(" ");
            }
            time.append(minutes);
            time.append(" min.");
            millis -= minutes * 60000L;
        }

        long seconds = millis / 1000L;
        if (seconds != 0L) {
            if (time.length() > 0) {
                time.append(" ");
            }
            time.append(seconds);
            time.append(" sec.");
            millis -= seconds * 1000L;
        }

        if (millis != 0L) {
            if (time.length() > 0) {
                time.append(" ");
            }
            time.append(millis);
            time.append(" ms.");
        }

        if (time.length() == 0) {
            time.append("0 ms.");
        }

        return time.toString();
    }


    public static final int checkNulls(int nulls) {
        if (nulls >= 0 && nulls <= 100) {
            return nulls;
        }
        if (nulls > 100) {
            return 100;
        }
        return 0;
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonste\\util\Converter.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */