package pl.kernelpanic.dbmonster.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {
    Connection getConnection() throws SQLException;

    void testConnection() throws SQLException, Exception;

    void shutdown() throws SQLException;
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\connection\ConnectionProvider.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */