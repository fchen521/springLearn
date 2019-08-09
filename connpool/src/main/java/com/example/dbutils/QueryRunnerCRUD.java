package com.example.dbutils;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class QueryRunnerCRUD {

    public static <T> List<T> query(String sql, Class<T> t) throws SQLException {
        QueryRunner qr = new QueryRunner();
        List<T> ts = qr.query(JDBCDruidUtils.getConnection(), sql, new BeanListHandler<T>(t));
        return ts;
    }
    public static <T> List<T> query2(String sql, Class<T> t) throws SQLException {
        QueryRunner qr = new QueryRunner();
        List<T> ts = qr.query(JDBCDruidUtils.getConnection(), sql, new BeanListHandler<T>(t,new BasicRowProcessor()));
        return ts;
    }
}
