package itmo.programming.command;

import itmo.programming.requests.ClearRequest;
import itmo.programming.requests.Request;


/**
 * The type Clear prompt.
 */
public class ClearPrompt implements Promptable {

    @Override
    public Request createRequest(String parameter, String login) {
        if (login == null) {
            return null;
        }
        return new ClearRequest(login);
    }

}
