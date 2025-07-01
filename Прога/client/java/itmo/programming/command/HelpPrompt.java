package itmo.programming.command;

import itmo.programming.requests.HelpRequest;
import itmo.programming.requests.Request;

/**
 * The type Help prompt.
 */
public class HelpPrompt implements Promptable {

    @Override
    public Request createRequest(String parameter, String login) {
        if (login == null) {
            return null;
        }
        return new HelpRequest(login);
    }
}
