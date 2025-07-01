package itmo.programming.responses;

import itmo.programming.exceptions.FailedExecution;
import itmo.programming.message.OutputMessage;
import java.io.IOException;

/**
 * The interface Response.
 */
public sealed interface Response permits
        MessageResponse,
        ValidationResponse,
        AuthenticationResponse {
    /**
     * The type Message.
     */
    record Message(int id, Response response) {}

    /**
     * Accept.
     *
     * @param visitor the visitor
     * @return the output message
     * @throws FailedExecution        the failed execution
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    default OutputMessage accept(ResponseVisitor visitor) throws
            FailedExecution,
            IOException,
            ClassNotFoundException {
        if (this instanceof MessageResponse messageResponse) {
            return visitor.visit(messageResponse);
        } else if (this instanceof ValidationResponse validationResponse) {
            return visitor.visit(validationResponse);
        } else if (this instanceof AuthenticationResponse authenticationResponse) {
            return visitor.visit(authenticationResponse);
        }
        return null;
    }
}
