package itmo.programming.responses;

import itmo.programming.exceptions.FailedExecution;
import itmo.programming.message.OutputMessage;
import java.io.IOException;
import java.io.Serializable;

/**
 * The type Authentication response.
 */
public record AuthenticationResponse(boolean isValid, String message, String login)
        implements Response, Serializable {

    @Override
    public OutputMessage accept(ResponseVisitor visitor) throws
            FailedExecution,
            IOException,
            ClassNotFoundException {
        return visitor.visit(this);
    }
}
