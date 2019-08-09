package com.example.dbutils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCDruidUtils {
    private static DataSource source;

    static {
        try {
            /**
             * 加载配置文件
             */
            InputStream is = JDBCDruidUtils.class.getClassLoader().getResourceAsStream("druid.properties");
            Properties pro = new Properties();
            pro.load(is);
            /**
             * 获取数据源
             */
            source = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    public static DataSource getDataSouce() {
        return source;
    }
}
