package itmo.programming.connection.visitors;

import itmo.programming.connection.ObjectTransport;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.message.OutputMessage;
import itmo.programming.responses.AuthenticationResponse;
import itmo.programming.responses.MessageResponse;
import itmo.programming.responses.ResponseVisitor;
import itmo.programming.responses.ValidationResponse;
import java.io.IOException;

/**
 * The type Resend request visitor.
 */
public class ResendRequestVisitor implements ResponseVisitor {

    private final ObjectTransport objectTransport;

    /**
     * Instantiates a new Resend request visitor.
     *
     * @param objectTransport the object transport
     */
    public ResendRequestVisitor(ObjectTransport objectTransport) {
        this.objectTransport = objectTransport;
    }

    @Override
    public OutputMessage visit(MessageResponse response) {
        return new OutputMessage(response.message());
    }

    @Override
    public OutputMessage visit(ValidationResponse response) throws
            FailedExecution,
            IOException,
            ClassNotFoundException {
        return new OutputMessage(objectTransport.sendPackageWithParam(response));
    }

    @Override
    public OutputMessage visit(AuthenticationResponse response) {
        return new OutputMessage(response.message(), response.isValid(), response.login());
    }
}
