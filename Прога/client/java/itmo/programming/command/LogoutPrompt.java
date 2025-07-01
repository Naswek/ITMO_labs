package itmo.programming.command;

import itmo.programming.exceptions.FailedExecution;
import itmo.programming.exepctions.LogoutException;
import itmo.programming.requests.Request;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * The type Logout prompt.
 */
public class LogoutPrompt implements Promptable {
    @Override
    public Request createRequest(String parameter, String login) throws
            FailedExecution, IOException {
        if (login == null) {
            return null;
        }
        throw new LogoutException("Вы вышли из пользователя: " + login);
    }
    Callable
}
