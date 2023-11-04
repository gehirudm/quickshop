package quickshop.db;

import java.sql.SQLException;

public class ConnectionShutDownHook extends Thread {

    private java.util.logging.Logger logger;

    public ConnectionShutDownHook(java.util.logging.Logger logger) {
        super();
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            Connection.disposeConnection();
        } catch (SQLException e) {
            logger.severe("Failed to shutdown Derby connection successfully");
        }
    }

}
