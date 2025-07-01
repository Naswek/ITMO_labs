package itmo.programming.command.validation;

import itmo.programming.requests.Request;
import itmo.programming.requests.ValidationIdRequest;
import itmo.programming.responses.ValidationResponse;
import itmo.programming.storage.ManageDatabase;
import itmo.programming.util.ManageCollection;

/**
 * The type Check index.
 */
public class CheckId implements CheckParameter {

    private final ManageCollection collection;
    private final ManageDatabase manageDatabase;

    /**
     * Instantiates a new Check index.
     *
     * @param collection the collection
     */
    public CheckId(ManageCollection collection, ManageDatabase manageDatabase) {
        this.collection = collection;
        this.manageDatabase = manageDatabase;
    }

    @Override
    public ValidationResponse checkParameter(Request request) {
        final ValidationIdRequest validationIdRequest
                = (ValidationIdRequest) request;

        final int userId = manageDatabase.getUserId(validationIdRequest.login());
        final int id = validationIdRequest.id();
        return new ValidationResponse(
                collection.isIdValid(id, userId),
                id,
                validationIdRequest.command(), validationIdRequest.login());
    }
}
