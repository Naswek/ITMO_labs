package itmo.programming.storage.interaction;

import itmo.programming.exceptions.DataAccessException;
import itmo.programming.exceptions.OwnershipException;
import itmo.programming.object.Car;
import itmo.programming.object.Coordinates;
import itmo.programming.object.HumanBeing;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

public class UpdateObjects {

    private static final String SELECT_FOREIGN_KEYS_SQL =
            "SELECT coord_id, car_id FROM humans WHERE id = ?";
    private static final String UPDATE_COORDINATES_SQL =
            "UPDATE coordinates SET x = ?, y = ? WHERE id = ?";
    private static final String UPDATE_CAR_SQL =
            "UPDATE cars SET name = ?, is_cool = ? WHERE id = ?";
    private static final String UPDATE_HUMAN_SQL =
            "UPDATE humans SET "
                    + "name = ?, coord_id = ?, creation_date = ?, real_hero = ?, "
                    + "has_toothpick = ?, impact_speed = ?, soundtrack = ?, "
                    + "weapon = ?::weapon_type, mood = ?::mood_type, "
                    + "car_id = ?, creator_id = ? "
                    + "WHERE id = ? "
                    + "RETURNING id, name, coord_id, creation_date, real_hero, has_toothpick, "
                    + "impact_speed, soundtrack, weapon, mood, car_id, creator_id";

    private static final int NAME_INDEX = 1;
    private static final int COORD_ID_INDEX = 2;
    private static final int CREATION_DATE_INDEX = 3;
    private static final int REAL_HERO_INDEX = 4;
    private static final int TOOTHPICK_INDEX = 5;
    private static final int IMPACT_SPEED_INDEX = 6;
    private static final int SOUNDTRACK_INDEX = 7;
    private static final int WEAPON_INDEX = 8;
    private static final int MOOD_INDEX = 9;
    private static final int CAR_ID_INDEX_HUMAN = 10;
    private static final int CREATOR_ID_INDEX = 11;
    private static final int HUMAN_ID_INDEX = 12;
    private static final int X_INDEX = 1;
    private static final int Y_INDEX = 2;
    private static final int COORDINATE_ID_INDEX = 3;
    private static final int CAR_NAME_INDEX = 1;
    private static final int CAR_IS_COOL_INDEX = 2;
    private static final int CAR_ID_INDEX = 3;
    private final Connection connection;
    private final UserInteraction userInteraction;

    public UpdateObjects(final Connection connection, final UserInteraction userInteraction) {
        this.connection = connection;
        this.userInteraction = userInteraction;
    }

    public HumanBeing update(final HumanBeing human, final int id, final String login, final int userId)
            throws SQLException {

        if (!isObjIdValid(id, userId)) {
            throw new OwnershipException("Вам не принадлежит объект с id: " + id);
        }

        try (PreparedStatement statement = connection.prepareStatement(SELECT_FOREIGN_KEYS_SQL)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    final int coordId = resultSet.getInt("coord_id");
                    final int carId = resultSet.getInt("car_id");

                    updateCoordinates(coordId, human.getCoordinates());
                    updateCar(carId, human.getCar());
                    return updateHuman(human, coordId, carId, login, id);
                } else {
                    throw new SQLException("Не удалось найти человека с ID: " + id);
                }
            }
        }
    }

    public void updateCoordinates(final int coordId, final Coordinates coordinates)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_COORDINATES_SQL)) {
            statement.setLong(X_INDEX, coordinates.getFirstElement());
            statement.setFloat(Y_INDEX, coordinates.getSecondElement());
            statement.setInt(COORDINATE_ID_INDEX, coordId);
            statement.executeUpdate();
        }
    }

    public void updateCar(final int carId, final Car car) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_CAR_SQL)) {
            statement.setString(CAR_NAME_INDEX, car.getName());
            statement.setBoolean(CAR_IS_COOL_INDEX, car.isCool());
            statement.setInt(CAR_ID_INDEX, carId);
            statement.executeUpdate();
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

    public HumanBeing updateHuman(
            final HumanBeing human,
            final int coordId,
            final int carId,
            final String login,
            final int id
    ) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_HUMAN_SQL)) {
            statement.setString(NAME_INDEX, human.getName());
            statement.setInt(COORD_ID_INDEX, coordId);
            statement.setTimestamp(CREATION_DATE_INDEX, Timestamp.valueOf(human.getCreationDate()));
            statement.setBoolean(REAL_HERO_INDEX, human.isRealHero());
            statement.setObject(TOOTHPICK_INDEX, human.isHasToothpick(), Types.BOOLEAN);
            statement.setFloat(IMPACT_SPEED_INDEX, human.getImpactSpeed());
            statement.setString(SOUNDTRACK_INDEX, human.getSoundtrackName());
            statement.setString(WEAPON_INDEX, human.getWeaponType().name());
            statement.setString(MOOD_INDEX, human.getMood().name());
            statement.setInt(CAR_ID_INDEX_HUMAN, carId);
            statement.setInt(CREATOR_ID_INDEX, userInteraction.getUserId(login));
            statement.setInt(HUMAN_ID_INDEX, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    human.setId(rs.getInt("id"));
                    human.setName(rs.getString("name"));
                    human.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                    human.setIsRealHero(rs.getBoolean("real_hero"));
                    human.setIsHasToothpick(rs.getBoolean("has_toothpick"));
                    human.setImpactSpeed(rs.getFloat("impact_speed"));
                    human.setSoundtrackName(rs.getString("soundtrack"));
                    human.setWeaponType(Enum.valueOf(human.getWeaponType().getDeclaringClass(),
                            rs.getString("weapon")));
                    if (rs.getString("mood") != null) {
                        human.setMood(Enum.valueOf(human.getMood().getDeclaringClass(),
                                rs.getString("mood")));
                    } else {
                        human.setMood(null);
                    }
                    human.setCreatorId(rs.getInt("creator_id"));
                    return human;
                } else {
                    throw new SQLException("Не удалось получить обновлённого "
                            + "человека после обновления.");
                }
            }
        }
    }
}
