package com.example.dbutils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCHikariUtils {
    private static String DRIVER = null;
    private static String URL = null;
    private static String USER = null;
    private static String PWD = null;
    private static DataSource dataSource;

    static {
        /**
         * 加载配置文件
         */
        Properties pro = null;
        try {
            InputStream is = JDBCDruidUtils.class.getClassLoader().getResourceAsStream("application.properties");
            pro = new Properties();
            pro.load(is);
            DRIVER = pro.getProperty("spring.datasource.driver-class-name");
            URL = pro.getProperty("spring.datasource.url");
            USER = pro.getProperty("spring.datasource.username");
            PWD = pro.getProperty("spring.datasource.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 获取数据源
         */
        dataSource = new HikariDataSource(new HikariConfig(pro));
    }

    public Connection getConnection() throws SQLException {
       return dataSource.getConnection();
    }

    public DataSource getDataSource(){
        return dataSource;
    }
}
