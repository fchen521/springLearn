package pl.kernelpanic.dbmonster.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Transaction {
    private Connection conn;
    private Statement stmt;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private ConnectionProvider connProvider;

    public Transaction(ConnectionProvider cp) {
        this.conn = null;


        this.stmt = null;


        this.pstmt = null;


        this.rs = null;


        this.connProvider = null;


        this.connProvider = cp;
    }


    public final Connection begin() throws SQLException {
        this.conn = this.connProvider.getConnection();
        return this.conn;
    }


    public final void commit() throws SQLException {
        if (this.rs != null) {
            this.rs.close();
        }

        if (this.stmt != null) {
            this.stmt.close();
        }

        if (!this.conn.getAutoCommit()) {
            this.conn.commit();
        }
    }


    public final void abort() throws SQLException {
        try {
            this.conn.rollback();
        } catch (Exception e) {
        }
    }


    public final void close() throws SQLException {
        if (this.rs != null) {
            try {
                this.rs.close();
            } catch (SQLException e) {

            } finally {
                this.rs = null;
            }
        }

        if (this.stmt != null) {
            try {
                this.stmt.close();
            } catch (SQLException e) {

            } finally {
                this.stmt = null;
            }
        }

        if (this.pstmt != null) {
            try {
                this.pstmt.close();
            } catch (SQLException e) {

            } finally {
                this.pstmt = null;
            }
        }

        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {

            } finally {
                this.conn = null;
            }
        }
    }


    public final ResultSet executeQuery(String query) throws SQLException {
        this.stmt = this.conn.createStatement();
        this.rs = this.stmt.executeQuery(query);
        return this.rs;
    }


    public final PreparedStatement prepareStatement(String query) throws SQLException {
        this.conn.setAutoCommit(false);
        this.pstmt = this.conn.prepareStatement(query);
        return this.pstmt;
    }

    public final void executeBatch() throws SQLException {
        if (this.pstmt != null)
            this.pstmt.executeBatch();
    }

    public final void execute() throws SQLException {
        if (this.pstmt != null)
            this.pstmt.execute();
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\connection\Transaction.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */