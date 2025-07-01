package itmo.programming.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The type Connection db.
 */
public class ConnectionDb implements AutoCloseable {

    private final Connection connection;

    /**
     * Instantiates a new Connection db.
     *
     * @throws SQLException the sql exception
     */
    public ConnectionDb() throws SQLException {
        this.connection = DriverManager.getConnection(
                "jdbc:postgresql://db:5432/studs", "s407893", "g2GkAU2hHzjIzd6m");
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return this.connection;
    }
}
