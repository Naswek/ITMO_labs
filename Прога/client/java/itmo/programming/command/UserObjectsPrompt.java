package itmo.programming.command;

import itmo.programming.exceptions.FailedExecution;
import itmo.programming.requests.Request;
import itmo.programming.requests.UserObjectsRequest;
import java.io.IOException;

/**
 * The type User objects prompt.
 */
public class UserObjectsPrompt implements Promptable {

    @Override
    public Request createRequest(String parameter, String login) throws
            FailedExecution, IOException {
        if (login == null) {
            return null;
        }
        return new UserObjectsRequest(login);
    }
}
