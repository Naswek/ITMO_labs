package itmo.programming.command;

import itmo.programming.creator.PromptObjects;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.requests.AddRequest;
import itmo.programming.requests.Request;

/**
 * The type Add prompt.
 */
public class AddPrompt implements Promptable {
    private final PromptObjects objectCreator;

    /**
     * Instantiates a new Add prompt.
     *
     * @param objectCreator the object creator
     */
    public AddPrompt(PromptObjects objectCreator) {
        this.objectCreator = objectCreator;
    }

    @Override
    public Request createRequest(String parameter, String login) throws FailedExecution {
        if (login == null) {
            return null;
        }
        return new AddRequest(objectCreator.createHuman(), login);
    }
}
