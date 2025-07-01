package itmo.programming.command;

import itmo.programming.exceptions.ExitCommandException;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.requests.Request;
import java.io.IOException;

/**
 * The type Exit prompt.
 */
public class ExitPrompt implements Promptable {

    @Override
    public Request createRequest(String parameter, String login)
            throws FailedExecution, IOException {
        if (login == null) {
            return null;
        }
        throw new ExitCommandException("Клиент завершил работу");
    }
}
