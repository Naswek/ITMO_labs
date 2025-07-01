package itmo.programming.command;

import itmo.programming.requests.Request;
import itmo.programming.requests.SortRequest;


/**
 * The type Sort prompt.
 */
public class SortPrompt implements Promptable {


    @Override
    public Request createRequest(String parameter, String login) {
        if (login == null) {
            return null;
        }
        return new SortRequest(login);
    }
}
