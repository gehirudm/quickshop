package quickshop.db;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    private static java.sql.Connection conn;

    public static void createConnection(String url, String derbyHome) throws SQLException {
        System.setProperty("derby.system.home", derbyHome);
        conn = DriverManager.getConnection(url);
    }

    public static void createConnection() throws SQLException {
        createConnection("jdbc:derby:testdb;create=true", "C:\\derby");
    }

    public static java.sql.Connection getConnection() {
        return conn;
    }

    public static void disposeConnection() throws SQLException {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            // Matching these 2 conditions mean Derby was shutdown successfully
            if (!((ex.getErrorCode() == 50000) && ("XJ015".equals(ex.getSQLState()))))
                throw ex;
        } finally {
            if (conn != null)
                conn.close();
        }
    }
}
