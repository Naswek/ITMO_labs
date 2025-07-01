package itmo.programming.storage.interaction;

import itmo.programming.object.HumanBeing;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * Provides methods to add objects to the database.
 */
public class AddObject {

    private static final String INSERT_COORDINATES_SQL =
            "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
    private static final int COORDINATES_PARAM_X = 1;
    private static final int COORDINATES_PARAM_Y = 2;
    private static final String INSERT_CAR_SQL =
            "INSERT INTO cars (name, is_cool) VALUES (?, ?) RETURNING id";
    private static final int CAR_PARAM_NAME = 1;
    private static final int CAR_PARAM_IS_COOL = 2;
    private static final String INSERT_HUMAN_SQL =
            "INSERT INTO humans ("
                    + "name, coord_id, creation_date, real_hero, has_toothpick, "
                    + "impact_speed, soundtrack, weapon, mood, car_id, creator_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?::weapon_type, ?::mood_type, ?, ?) RETURNING id";
    private static final int HUMAN_PARAM_NAME = 1;
    private static final int HUMAN_PARAM_COORD_ID = 2;
    private static final int HUMAN_PARAM_CREATION_DATE = 3;
    private static final int HUMAN_PARAM_REAL_HERO = 4;
    private static final int HUMAN_PARAM_HAS_TOOTHPICK = 5;
    private static final int HUMAN_PARAM_IMPACT_SPEED = 6;
    private static final int HUMAN_PARAM_SOUNDTRACK = 7;
    private static final int HUMAN_PARAM_WEAPON = 8;
    private static final int HUMAN_PARAM_MOOD = 9;
    private static final int HUMAN_PARAM_CAR_ID = 10;
    private static final int HUMAN_PARAM_CREATOR_ID = 11;
    private static final String SUCCESS_MESSAGE = "Человек успешно добавлен в базу данных.";
    private static final String ERROR_INSERT_COORDINATES = "Не удалось вставить координаты";
    private static final String ERROR_INSERT_CAR = "Не удалось вставить автомобиль";
    private static final String ERROR_SAVE_HUMAN =
            "Ошибка при добавлении человека. Транзакция откатилась.";
    private final Connection connection;
    private final UserInteraction userInteraction;

    /**
     * Constructs an AddObject with the specified connection and user interaction.
     *
     * @param connection      the database connection
     * @param userInteraction the user interaction handler
     */
    public AddObject(Connection connection, UserInteraction userInteraction) {
        this.connection = connection;
        this.userInteraction = userInteraction;
    }

    /**
     * Saves the given human to the database.
     *
     * @param human the human being to save
     * @param login the login of the user performing the operation
     * @throws SQLException if a database error occurs
     */
    public HumanBeing saveHuman(HumanBeing human, String login) throws SQLException {
        final boolean previousAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);

        try {
            final int coordId = insertCoordinates(human);
            final int carId = insertCar(human);

            connection.commit();
            return insertHuman(human, coordId, carId, login);
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException(ERROR_SAVE_HUMAN, e);
        } finally {
            connection.setAutoCommit(previousAutoCommit);
        }
    }

    private int insertCoordinates(HumanBeing human) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_COORDINATES_SQL)) {
            statement.setLong(COORDINATES_PARAM_X, human.getCoordinates().getFirstElement());
            statement.setFloat(COORDINATES_PARAM_Y, human.getCoordinates().getSecondElement());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    throw new SQLException(ERROR_INSERT_COORDINATES);
                }
            }
        }
    }

    private int insertCar(HumanBeing human) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_CAR_SQL)) {
            statement.setString(CAR_PARAM_NAME, human.getCar().getName());
            statement.setBoolean(CAR_PARAM_IS_COOL, human.getCar().isCool());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    throw new SQLException(ERROR_INSERT_CAR);
                }
            }
        }
    }

    private HumanBeing insertHuman(HumanBeing human, int coordinateId, int carId, String login) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_HUMAN_SQL)) {
            statement.setString(HUMAN_PARAM_NAME, human.getName());
            statement.setInt(HUMAN_PARAM_COORD_ID, coordinateId);
            statement.setTimestamp(HUMAN_PARAM_CREATION_DATE, Timestamp.valueOf(human.getCreationDate()));
            statement.setBoolean(HUMAN_PARAM_REAL_HERO, human.isRealHero());
            statement.setObject(HUMAN_PARAM_HAS_TOOTHPICK, human.isHasToothpick(), Types.BOOLEAN);
            statement.setFloat(HUMAN_PARAM_IMPACT_SPEED, human.getImpactSpeed());
            statement.setString(HUMAN_PARAM_SOUNDTRACK, human.getSoundtrackName());
            statement.setString(HUMAN_PARAM_WEAPON, human.getWeaponType().name());
            statement.setString(HUMAN_PARAM_MOOD, human.getMood().name());
            statement.setInt(HUMAN_PARAM_CAR_ID, carId);

            int creatorId = userInteraction.getUserId(login);
            statement.setInt(HUMAN_PARAM_CREATOR_ID, creatorId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    human.setId(id);
                    human.setCreatorId(creatorId);
                    return human;
                } else {
                    throw new SQLException("Не удалось получить ID после вставки.");
                }
            }
        }
    }
}
