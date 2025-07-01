package itmo.programming.command.validation;

import itmo.programming.requests.Request;
import itmo.programming.responses.AuthenticationResponse;
import java.sql.SQLException;

/**
 * The interface Check user.
 */
public interface CheckUser {

    /**
     * Check user authentication response.
     *
     * @param request the request
     * @return the authentication response
     * @throws SQLException the sql exception
     */
    AuthenticationResponse checkUser(Request request);
}
