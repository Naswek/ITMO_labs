package itmo.programming.command;

import itmo.programming.creator.PromptObjects;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.exceptions.RequestCreationException;
import itmo.programming.requests.Request;
import itmo.programming.requests.UpdateRequest;
import itmo.programming.requests.ValidationIdRequest;
import itmo.programming.responses.ValidationResponse;

/**
 * The type Update prompt.
 */
public class UpdatePrompt implements Promptable, ParamCheckable {

    private final PromptObjects humanCreator;

    /**
     * Instantiates a new Update prompt.
     *
     * @param humanCreator the human creator
     */
    public UpdatePrompt(PromptObjects humanCreator) {
        this.humanCreator = humanCreator;
    }

    @Override
    public Request createRequest(String parameter, String login) throws FailedExecution {
        if (login == null) {
            return null;
        }
        if (isParameterValid(parameter)) {
            return new ValidationIdRequest(Integer.parseInt(parameter), "updateById", login);
        }
        throw new RequestCreationException("параметр должен быть числом");
    }

    @Override
    public boolean isParameterValid(String parameter) {
        return parameter != null && parameter.matches("^-?\\d+$");
    }

    /**
     * Instantiates a new Update prompt.
     *
     */
    public Request checkParameter(ValidationResponse response) throws FailedExecution {
        return new UpdateRequest(response.parameter(),
                humanCreator.createHuman(), response.login());
    }
}
