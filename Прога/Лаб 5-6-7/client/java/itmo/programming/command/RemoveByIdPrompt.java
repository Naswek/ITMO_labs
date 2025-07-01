package itmo.programming.command;

import itmo.programming.exceptions.RequestCreationException;
import itmo.programming.requests.RemoveByIdRequest;
import itmo.programming.requests.Request;
import itmo.programming.requests.ValidationIdRequest;
import itmo.programming.responses.ValidationResponse;

/**
 * The type Remove by index prompt.
 */
public class RemoveByIdPrompt implements Promptable, ParamCheckable {

    @Override
    public Request createRequest(String parameter, String login) {
        if (login == null) {
            return null;
        }

        if (isParameterValid(parameter)) {

            return new ValidationIdRequest(Integer.parseInt(parameter), "remove_by_id", login);
        }
        throw new RequestCreationException("Введите число в параметры команды");
    }

    @Override
    public boolean isParameterValid(String parameter) {
        return parameter != null && parameter.matches("^-?\\d+$");
    }

    @Override
    public Request checkParameter(ValidationResponse response) {
        return new RemoveByIdRequest(response.parameter(), response.login());
    }
}



