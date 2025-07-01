package itmo.programming.storage.interaction;

import itmo.programming.responses.AuthenticationResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides interaction with user-related operations in the database.
 */
public class UserInteraction {

    private final Connection connection;

    /**
     * Constructs a new UserInteraction.
     *
     * @param connection the database connection
     */
    public UserInteraction(final Connection connection) {
        this.connection = connection;
    }

    /**
     * Returns the user ID for the given login.
     *
     * @param login the user's login
     * @return the user ID
     * @throws SQLException if user not found or a database error occurs
     */
    public int getUserId(final String login) throws SQLException {
        final String sql = "SELECT id FROM users WHERE login = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Пользователь не найден: " + login);
                }
            }
        }
    }

    /**
     * Registers a user in the database.
     *
     * @param login the login
     * @param password the password
     * @return an authentication response with the result
     * @throws SQLException if a database error occurs
     */
    public AuthenticationResponse registerUser(final String login, final String password)
            throws SQLException {
        if (checkUserLogin(login)) {
            return new AuthenticationResponse(false, "Пользователь с таким логином уже существует", login);
        }

        final String sql = "INSERT INTO users (login, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.executeUpdate();
        }

        return new AuthenticationResponse(true, "Регистрация прошла успешно", login);
    }

    /**
     * Checks user's login and password credentials.
     *
     * @param login the login
     * @param password the password
     * @return an authentication response with the result
     * @throws SQLException if a database error occurs
     */
    public AuthenticationResponse checkUser(final String login, final String password)
            throws SQLException {
        if (!checkUserLogin(login)) {
            return new AuthenticationResponse(false, "Пользователь с таким логином не найден", login);
        }

        if (!checkUserPassword(password)) {
            return new AuthenticationResponse(false, "Неверно введен пароль", login);
        }

        return new AuthenticationResponse(true, "Успешная авторизация", login);
    }

    private boolean checkUserLogin(final String login) throws SQLException {
        final String sql = "SELECT 1 FROM users WHERE login = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean checkUserPassword(final String password) throws SQLException {
        final String sql = "SELECT 1 FROM users WHERE password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
