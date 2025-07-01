package itmo.programming.command;

import itmo.programming.exceptions.RequestCreationException;
import itmo.programming.requests.CountGreaterThanCarRequest;
import itmo.programming.requests.Request;

/**
 * The type Count greater than car prompt.
 */
public class CountGreaterThanCarPrompt implements Promptable {

    @Override
    public Request createRequest(String parameter, String login) {
        if (login == null) {
            return null;
        }
        if (parameter != null && !parameter.equals(" ")) {
            return new CountGreaterThanCarRequest(parameter, login);
        }
        throw new RequestCreationException("введите параметр для команды");
    }
}
