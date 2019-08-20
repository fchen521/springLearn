package com.example.utils;

public final class Constants {
    public static final String SPRING_DATASOURCE_DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";

    public static final String SPRING_DATASOURCE_URL = "spring.datasource.url";

    public static final String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";

    public static final String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";
}

class mains{
    public static void main(String[] args) {
        System.out.println(Constants.SPRING_DATASOURCE_DRIVER_CLASS_NAME);
    }
}
