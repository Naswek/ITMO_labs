package itmo.programming.command;

import itmo.programming.exceptions.RequestCreationException;
import itmo.programming.requests.CountGreaterThanCarRequest;
import itmo.programming.requests.Request;


/**
 * The type Count less than soundtrack name prompt.
 */
public class CountLessThanSoundtrackNamePrompt implements Promptable {

    @Override
    public Request createRequest(String parameter, String login) throws RequestCreationException {
        if (login == null) {
            return null;
        }
        if (parameter != null && !parameter.equals(" ")) {
            return new CountGreaterThanCarRequest(parameter, login);
        }
        throw new RequestCreationException("введите параметр для команды");
    }
}
