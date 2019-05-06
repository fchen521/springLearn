package com.example.thymeleaf.utils;

public class MysqlDBHelper {
    /*private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/thy?serverTimezone=GMT%2B8&useSSL=false";
    private String user = "root";
    private String password = "root";
    private Connection conn;
    private Statement stmt;

    public MysqlDBHelper() {
        init();
    }

    public void init()  {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public Map<String, Object> queryWithObject(String sql) throws SQLException {
        Map<String, Object> obj = new HashMap<String, Object>();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        if(rs.next()) {
            for(int i = 1; i <= columnCount; i++) {
                obj.put(md.getColumnLabel(i), rs.getString(i));
            }
        }
        if(rs != null) {
            rs.close();
        }
        return obj;
    }

    public void close(Connection conn, Statement stmt) throws SQLException {
        if(conn != null) {
            conn.close();
        }
        if(stmt != null) {
            stmt.close();
        }
    }

    public static void main(String[] args) throws SQLException {
        MysqlDBHelper helper =new MysqlDBHelper();
        Map<String, Object> map = helper.queryWithObject("select * from t_logs");
        for (Object o : map.values()) {
            System.out.println(o);
        }
    }*/
}
