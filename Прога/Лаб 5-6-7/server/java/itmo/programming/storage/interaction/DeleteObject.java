package itmo.programming.storage.interaction;

import itmo.programming.exceptions.DataAccessException;
import itmo.programming.exceptions.OwnershipException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides methods to delete objects from the database.
 */
public class DeleteObject {

    private static final String SELECT_FOREIGN_KEYS_SQL =
            "SELECT coord_id, car_id FROM humans WHERE id = ? AND creator_id = "
                    + "(SELECT id FROM users WHERE login = ?)";
    private static final String DELETE_HUMAN_SQL =
            "DELETE FROM humans WHERE id = ? AND creator_id = "
                    + "(SELECT id FROM users WHERE login = ?)";
    private static final String DELETE_COORDINATE_SQL = "DELETE FROM coordinates WHERE id = ?";
    private static final String DELETE_CAR_SQL = "DELETE FROM cars WHERE id = ?";
    private static final int HUMAN_ID_INDEX = 1;
    private static final int LOGIN_INDEX = 2;
    private static final int COORD_ID_INDEX = 1;
    private static final int CAR_ID_INDEX = 1;
    private final Connection connection;
    private final UserInteraction userInteraction;

    /**
     * Constructs a DeleteObject with the specified connection and user interaction.
     *
     * @param connection      the database connection
     * @param userInteraction the user interaction handler
     */
    public DeleteObject(Connection connection, UserInteraction userInteraction) {
        this.connection = connection;
        this.userInteraction = userInteraction;
    }

    /**
     * Removes a human and related entries by ID and login.
     *
     * @param id    the human ID
     * @param login the user login
     * @throws SQLException if a database error occurs
     */
    public void removeById(final int id, final String login, final int userId) throws SQLException {

        if (!isObjIdValid(id, userId)) {
            throw new OwnershipException("Вам не принадлежит объект с id: " + id);
        }

        connection.setAutoCommit(false);

        try {
            final ForeignKeys keys = getForeignKeysForHuman(id, login);

            deleteHuman(id, login);

            if (keys != null) {
                if (keys.getCoordId() != null) {
                    deleteCoordinate(keys.getCoordId());
                }
                if (keys.getCarId() != null) {
                    deleteCar(keys.getCarId());
                }
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private boolean isObjIdValid(int objId, int userId) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT 1 FROM humans WHERE id = ? AND creator_id = ?")) {
            stmt.setInt(1, objId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при проверке владельца объекта", e);
        }
    }


    private ForeignKeys getForeignKeysForHuman(final int humanId, final String login)
            throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_FOREIGN_KEYS_SQL)) {
            stmt.setInt(HUMAN_ID_INDEX, humanId);
            stmt.setString(LOGIN_INDEX, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    final Integer coordId = rs.getObject("coord_id", Integer.class);
                    final Integer carId = rs.getObject("car_id", Integer.class);
                    return new ForeignKeys(coordId, carId);
                }
            }
        }
        throw new SQLException(
                "Failed to get foreign keys for human with index: " + humanId
                        + " and login: " + login);
    }

    private void deleteHuman(final int humanId, final String login) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_HUMAN_SQL)) {
            stmt.setInt(HUMAN_ID_INDEX, humanId);
            stmt.setString(LOGIN_INDEX, login);
            stmt.executeUpdate();
        }
    }

    private void deleteCoordinate(final int coordId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_COORDINATE_SQL)) {
            stmt.setInt(COORD_ID_INDEX, coordId);
            stmt.executeUpdate();
        }
    }

    private void deleteCar(final int carId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_CAR_SQL)) {
            stmt.setInt(CAR_ID_INDEX, carId);
            stmt.executeUpdate();
        }
    }
}
