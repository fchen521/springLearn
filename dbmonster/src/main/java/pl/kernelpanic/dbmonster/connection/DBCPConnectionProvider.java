package pl.kernelpanic.dbmonster.connection;

import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;


public class DBCPConnectionProvider
        implements ConnectionProvider, LogEnabled {
    private String driver;
    private String url;
    private String username;
    private String password;
    private Properties properties;
    private DataSource dataSource;
    private Log logger;
    private ObjectPool pool;

    public DBCPConnectionProvider() throws Exception {
        this.driver = System.getProperty("dbmonster.jdbc.driver");


        this.url = System.getProperty("dbmonster.jdbc.url");


        this.username = System.getProperty("dbmonster.jdbc.username");


        this.password = System.getProperty("dbmonster.jdbc.password");


        this.properties = null;


        this.dataSource = null;


        this.logger = null;


        initDriver();
        setupPool();
    }


    public DBCPConnectionProvider(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword) throws Exception {
        this.driver = System.getProperty("dbmonster.jdbc.driver");
        this.url = System.getProperty("dbmonster.jdbc.url");
        this.username = System.getProperty("dbmonster.jdbc.username");
        this.password = System.getProperty("dbmonster.jdbc.password");
        this.properties = null;
        this.dataSource = null;
        this.logger = null;
        this.driver = jdbcDriver;
        this.url = jdbcUrl;
        this.username = jdbcUsername;
        this.password = jdbcPassword;
        initDriver();
        setupPool();
    }


    public DBCPConnectionProvider(String jdbcDriver, String jdbcUrl, Properties props) throws Exception {
        this.driver = System.getProperty("dbmonster.jdbc.driver");
        this.url = System.getProperty("dbmonster.jdbc.url");
        this.username = System.getProperty("dbmonster.jdbc.username");
        this.password = System.getProperty("dbmonster.jdbc.password");
        this.properties = null;
        this.dataSource = null;
        this.logger = null;
        this.driver = jdbcDriver;
        this.url = jdbcUrl;
        this.properties = props;
        initDriver();
        setupPool();
    }


    public final Connection getConnection() throws SQLException {
        if (this.dataSource == null) {
            throw new SQLException("Data source is null!");
        }
        Connection conn = this.dataSource.getConnection();
        conn.setAutoCommit(true);
        return conn;
    }


    @SuppressWarnings("Duplicates")
    public final void testConnection() throws Exception {
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

        try {
            this.pool.close();
        } catch (Exception e) {
        } finally {
            this.pool = null;
        }

        this.dataSource = null;
        this.driver = null;
        this.logger = null;
        this.password = null;
        this.properties = null;
        this.url = null;
        this.username = null;
    }


    private final void initDriver() throws Exception {
        Class.forName(this.driver);
    }


    private final void setupPool() throws Exception {
        DriverManagerConnectionFactory driverManagerConnectionFactory = null;
        if (this.properties != null) {
            DriverManagerConnectionFactory driverManagerConnectionFactory1 = new DriverManagerConnectionFactory(this.url, this.properties);
        } else {
            driverManagerConnectionFactory = new DriverManagerConnectionFactory(this.url, this.username, this.password);
        }

        PoolableConnectionFactory factory = new PoolableConnectionFactory(driverManagerConnectionFactory, null);
        this.pool = new GenericObjectPool(factory);

        this.dataSource = new PoolingDataSource(this.pool);
    }
}