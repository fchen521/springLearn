package com.example.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseUtils {
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://localhost:1433;DataBaseName=xxx";
    private static final String USER = "xx";
    private static final String PWD = "xxx";

    public static Connection conn = null;
    public static PreparedStatement pst = null;

    static {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PWD);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public boolean zsg(String sql, Object... obj) {
        try {
            pst = conn.prepareStatement(sql);
            for (int i = 0; i < obj.length; i++) {
                pst.setObject(i + 1, obj[i]);
            }
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }

    public ResultSet select(String sql, Object... obj) throws SQLException {
        pst = conn.prepareStatement(sql);
        for (int i = 0; i < obj.length; i++) {
            pst.setObject(i + 1, obj[i]);
        }
        return pst.executeQuery();

    }

    public static void close() {
        try {
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
