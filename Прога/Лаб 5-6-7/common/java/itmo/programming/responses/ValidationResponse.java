package itmo.programming.responses;

import itmo.programming.exceptions.FailedExecution;
import itmo.programming.message.OutputMessage;
import java.io.IOException;
import java.io.Serializable;

/**
 * The type Validation response.
 */
public record ValidationResponse(boolean isExist, int parameter, String command, String login)
        implements Serializable, Response {

    @Override
    public OutputMessage accept(ResponseVisitor visitor) throws
            FailedExecution,
            IOException,
            ClassNotFoundException {
        return visitor.visit(this);
    }

}
