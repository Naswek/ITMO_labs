package itmo.programming.storage.interaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Provides methods to clear database entries.
 */
public class ClearDb {

    private static final String DELETE_HUMANS_SQL = "DELETE FROM humans WHERE creator_id = ?";
    private final Connection connection;
    private final UserInteraction userInteraction;

    /**
     * Constructs a ClearDb with the specified connection and user interaction.
     *
     * @param connection      the database connection
     * @param userInteraction the user interaction handler
     */
    public ClearDb(Connection connection, UserInteraction userInteraction) {
        this.connection = connection;
        this.userInteraction = userInteraction;
    }

    /**
     * Clears the humans created by the specified user.
     *
     * @param login the login of the user whose humans should be deleted
     * @throws SQLException if a database error occurs
     */
    public void clear(String login) throws SQLException {
        final int userId = userInteraction.getUserId(login);

        try (PreparedStatement statement = connection.prepareStatement(DELETE_HUMANS_SQL)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }
}
