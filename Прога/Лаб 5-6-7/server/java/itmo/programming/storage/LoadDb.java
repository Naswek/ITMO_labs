package itmo.programming.storage;

import itmo.programming.exceptions.CollectionLoadException;
import itmo.programming.object.Car;
import itmo.programming.object.Coordinates;
import itmo.programming.object.HumanBeing;
import itmo.programming.object.Mood;
import itmo.programming.object.WeaponType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Load db.
 */
public class LoadDb {

    private final ConnectionDb connectionDb;

    /**
     * Instantiates a new Load db.
     *
     * @param connectionDb the connection db
     */
    public LoadDb(ConnectionDb connectionDb) {
        this.connectionDb = connectionDb;
    }

    /**
     * Load all humans list.
     *
     * @return the list
     * @throws SQLException the sql exception
     */
    public CopyOnWriteArrayList<HumanBeing> loadAllHumans() {
        final CopyOnWriteArrayList<HumanBeing> humans = new CopyOnWriteArrayList<>();

        final String sql = "SELECT h.*, c.x, c.y, car.name AS car_name, car.is_cool "
                + "FROM humans h "
                + "LEFT JOIN coordinates c ON h.coord_id = c.id "
                + "LEFT JOIN cars car ON h.car_id = car.id";

        try (PreparedStatement statement = connectionDb.getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                final HumanBeing human = new HumanBeing(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        new Coordinates(resultSet.getLong("x"), resultSet.getFloat("y")),
                        resultSet.getTimestamp("creation_date").toLocalDateTime(),
                        resultSet.getBoolean("real_hero"),
                        resultSet.getBoolean("has_toothpick"),
                        resultSet.getFloat("impact_speed"),
                        resultSet.getString("soundtrack"),
                        WeaponType.valueOf(resultSet.getString("weapon")),
                        Mood.valueOf(resultSet.getString("mood")),
                        new Car(resultSet.getString("car_name"), resultSet.getBoolean("is_cool")),
                        resultSet.getInt("creator_id"));

                humans.add(human);
            }
        } catch (SQLException e) {
            throw new CollectionLoadException("Ошибка при загрузке коллекции из базы данных: "
                    + e.getMessage());
        }
        System.out.println("Коллекция успешно загружена!");
        return humans;
    }

}
