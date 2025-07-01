package itmo.programming.command;

import itmo.programming.exceptions.FailedParameterValidationException;
import itmo.programming.exceptions.RequestCreationException;
import itmo.programming.requests.RemoveByIndexRequest;
import itmo.programming.requests.Request;
import itmo.programming.requests.ValidaitionIndexRequest;
import itmo.programming.responses.ValidationResponse;

/**
 * The type Remove by index prompt.
 */
public class RemoveByIndexPrompt implements Promptable, ParamCheckable {

    @Override
    public Request createRequest(String parameter, String login) {
        if (login == null) {
            return null;
        }
        if (isParameterValid(parameter)) {
            return new ValidaitionIndexRequest(Integer.parseInt(parameter), "remove_at", login);
        }
        throw new RequestCreationException("Введите число в параметры команды");
    }

    @Override
    public boolean isParameterValid(String parameter) {
        return parameter != null && parameter.matches("^-?\\d+$");
    }

    @Override
    public Request checkParameter(ValidationResponse response) {
        if (response.isExist()) {
            return new RemoveByIndexRequest(
                    response.parameter(), response.login());
        }
        throw new FailedParameterValidationException("Объекта с таким index не существует");
    }
}
