package itmo.programming.command;

import itmo.programming.exceptions.RequestCreationException;
import itmo.programming.requests.FilterContainsNameRequest;
import itmo.programming.requests.Request;


/**
 * The type Filter contains name prompt.
 */
public class FilterContainsNamePrompt implements Promptable {

    @Override
    public Request createRequest(String parameter, String login) {
        if (login == null) {
            return null;
        }
        if (parameter != null && !parameter.equals(" ")) {
            return new FilterContainsNameRequest(parameter, login);
        }
        throw new RequestCreationException("введите параметр для команды");
    }
}
