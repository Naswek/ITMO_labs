package itmo.programming.command.validation;

import itmo.programming.requests.Request;
import itmo.programming.requests.ValidaitionIndexRequest;
import itmo.programming.responses.ValidationResponse;
import itmo.programming.util.ManageCollection;

/**
 * The type Check index.
 */
public class CheckIndex implements CheckParameter {

    private final ManageCollection collection;

    /**
     * Instantiates a new Check index.
     *
     * @param collection the collection
     */
    public CheckIndex(ManageCollection collection) {
        this.collection = collection;
    }

    @Override
    public ValidationResponse checkParameter(Request request) {
        final ValidaitionIndexRequest validaitionIndexRequest
                = (ValidaitionIndexRequest) request;



        final int index = validaitionIndexRequest.index();
        return new ValidationResponse(
                collection.isIndexValid(index),
                index,
                validaitionIndexRequest.command(), validaitionIndexRequest.login());
    }
}
