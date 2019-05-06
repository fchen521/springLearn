package com.example.thymeleaf.utils;

import java.util.Date;

public class DateUtils {

    public static int getCurrentUnixTime() {
        return getUnixTimeByDate(new Date());
    }

    public static int getUnixTimeByDate(Date date) {
        return (int)(date.getTime() / 1000L);
    }
}
