package itmo.programming.command;

import itmo.programming.requests.RemoveFirstRequest;
import itmo.programming.requests.Request;

/**
 * The type Remove first prompt.
 */
public class RemoveFirstPrompt implements Promptable {

    @Override
    public Request createRequest(String parameter, String login) {
        if (login == null) {
            return null;
        }
        return new RemoveFirstRequest(login);
    }

}
