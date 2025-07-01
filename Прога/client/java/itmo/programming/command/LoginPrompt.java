package itmo.programming.command;

import itmo.programming.connection.authentication.GettingUserData;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.requests.Request;
import java.io.IOException;

public class LoginPrompt implements Promptable {

    public final GettingUserData gettingUserData;

    public LoginPrompt(GettingUserData gettingUserData) {
        this.gettingUserData = gettingUserData;
    }

    @Override
    public Request createRequest(String parameter, String login) throws FailedExecution, IOException {
        return gettingUserData.startAuthentication();
    }
}
