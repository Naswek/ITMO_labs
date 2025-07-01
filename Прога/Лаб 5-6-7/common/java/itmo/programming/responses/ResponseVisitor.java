package itmo.programming.responses;

import itmo.programming.exceptions.FailedExecution;
import itmo.programming.message.OutputMessage;
import java.io.IOException;

/**
 * The interface Response visitor.
 */
public interface ResponseVisitor {

    /**
     * Visit.
     *
     * @param response the response
     * @return the output message
     */
    OutputMessage visit(MessageResponse response);

    /**
     * Visit.
     *
     * @param response the response
     * @return the output message
     * @throws FailedExecution        the failed execution
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    OutputMessage visit(ValidationResponse response) throws
            FailedExecution,
            IOException,
            ClassNotFoundException;

    /**
     * Visit output message.
     *
     * @param response the response
     * @return the output message
     * @throws FailedExecution        the failed execution
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    OutputMessage visit(AuthenticationResponse response) throws
            FailedExecution,
            IOException,
            ClassNotFoundException;
}
