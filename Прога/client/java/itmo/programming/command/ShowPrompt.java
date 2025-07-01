package itmo.programming.command;

import itmo.programming.requests.Request;
import itmo.programming.requests.ShowRequest;


/**
 * The type Show prompt.
 */
public class ShowPrompt implements Promptable {

    @Override
    public Request createRequest(String parameter, String login) {
        if (login == null) {
            return null;
        }
        return new ShowRequest(login);
    }
}
