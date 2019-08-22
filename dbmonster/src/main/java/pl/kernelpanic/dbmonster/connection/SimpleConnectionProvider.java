package pl.kernelpanic.dbmonster.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;


public class SimpleConnectionProvider
        implements ConnectionProvider, LogEnabled {
    private String driver;
    private String url;
    private String username;
    private String password;
    private Properties properties;
    private Log logger;

    public SimpleConnectionProvider() throws ClassNotFoundException {
        this.driver = System.getProperty("dbmonster.jdbc.driver");


        this.url = System.getProperty("dbmonster.jdbc.url");


        this.username = System.getProperty("dbmonster.jdbc.username");


        this.password = System.getProperty("dbmonster.jdbc.password");


        this.properties = null;


        this.logger = null;


        initDriver();
    }


    public SimpleConnectionProvider(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword) throws ClassNotFoundException {
        this.driver = System.getProperty("dbmonster.jdbc.driver");
        this.url = System.getProperty("dbmonster.jdbc.url");
        this.username = System.getProperty("dbmonster.jdbc.username");
        this.password = System.getProperty("dbmonster.jdbc.password");
        this.properties = null;
        this.logger = null;
        this.driver = jdbcDriver;
        this.url = jdbcUrl;
        this.username = jdbcUsername;
        this.password = jdbcPassword;
        initDriver();
    }


    public SimpleConnectionProvider(String jdbcDriver, String jdbcUrl, Properties props) throws ClassNotFoundException {
        this.driver = System.getProperty("dbmonster.jdbc.driver");
        this.url = System.getProperty("dbmonster.jdbc.url");
        this.username = System.getProperty("dbmonster.jdbc.username");
        this.password = System.getProperty("dbmonster.jdbc.password");
        this.properties = null;
        this.logger = null;
        this.driver = jdbcDriver;
        this.url = jdbcUrl;
        this.properties = props;
        initDriver();
    }


    public final Connection getConnection() throws SQLException {
        if (this.properties == null) {
            return DriverManager.getConnection(this.url, this.username, this.password);
        }
        return DriverManager.getConnection(this.url, this.properties);
    }


    public final void testConnection() throws ClassNotFoundException, SQLException {
        Connection conn = getConnection();
        DatabaseMetaData dbmd = conn.getMetaData();
        if (this.logger != null && this.logger.isInfoEnabled()) {
            this.logger.info("Today we are feeding: " + dbmd.getDatabaseProductName() + " " + dbmd.getDatabaseProductVersion());
        }


        conn.close();
        dbmd = null;
        conn = null;
    }


    public final void setLogger(Log log) {
        this.logger = log;
    }


    public final void shutdown()  {
        this.logger = null;
        this.driver = null;
        this.password = null;
        this.properties = null;
        this.url = null;
        this.username = null;
    }


    private final void initDriver() throws ClassNotFoundException {
        Class.forName(this.driver);
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\connection\SimpleConnectionProvider.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */