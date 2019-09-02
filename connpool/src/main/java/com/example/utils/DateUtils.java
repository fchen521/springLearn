package com.example.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

//高并发环境下使用ThreadLocal来限制SimpleDateFormat只能在线程内共享，这样就避免了多线程导致的线程安全问题
public class DateUtils {
    private static ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>();

    public static DateFormat getDateFormat() {
        DateFormat df = DATE_FORMAT.get();
        if (df == null) {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DATE_FORMAT.set(df);
        }
        return df;
    }

    /**
     *LocalDate:不包含具体时间的日期，比如2019-08-28。它可以用来存储生日，周年纪念日，入职日期等。
     – LocalTime:它代表的是不含日期的时间
     – LocalDateTime:它包含了日期及时间，不过还是没有偏移信息或者说时区。
     – ZonedDateTime:这是一个包含时区的完整的日期时间，偏移量是以UTC/格林威治时间为基准的。
     *
     *
     */

    public static void main(String[] args) {
        System.out.println(LocalTime.now());
        System.out.println(LocalDateTime.now());
        System.out.println(ZonedDateTime.now());
        System.out.println(new Date());
        System.out.println(new Date().getTime());
        System.out.println(System.currentTimeMillis());
    }

    //Date 转 LocalDateTime
    public static LocalDateTime dateConvertToLocalDateTime(Date date) {
        return date.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }


    /*
    LocalDateTime与String互转
    时间转字符串格式化
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    String dateTime = LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);

    字符串转时间
    String dateTimeStr = "2018-07-28 14:11:15";
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, df);
    */

}
