package itmo.programming.command.validation;

import itmo.programming.requests.AuthenticationRequest;
import itmo.programming.requests.Request;
import itmo.programming.responses.AuthenticationResponse;
import itmo.programming.storage.ManageDatabase;

/**
 * The type User credentials validation.
 */
public class UserCredentialsValidation implements CheckUser {

    private final ManageDatabase manageDatabase;

    /**
     * Instantiates a new User credentials validation.
     *
     * @param manageDatabase the manage database
     */
    public UserCredentialsValidation(ManageDatabase manageDatabase) {
        this.manageDatabase = manageDatabase;
    }

    @Override
    public AuthenticationResponse checkUser(Request request) {

        final var authenticationRequest = (AuthenticationRequest) request;
        return manageDatabase.processAuthentication(authenticationRequest);

    }
}
