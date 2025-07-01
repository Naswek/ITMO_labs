package itmo.programming.connection.visitors;

import itmo.programming.connection.ObjectTransport;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.message.OutputMessage;
import itmo.programming.responses.MessageResponse;
import itmo.programming.responses.Response;
import itmo.programming.responses.ValidationResponse;
import java.io.IOException;


/**
 * The type Visitor dispatch.
 */
public class VisitorDispatch {

    private final ResponsePrintVisitor printVisitor;
    private final ResendRequestVisitor resendVisitor;

    /**
     * Instantiates a new Visitor dispatch.
     *
     * @param objectTransport the object transport
     */
    public VisitorDispatch(ObjectTransport objectTransport) {
        this.printVisitor = new ResponsePrintVisitor();
        this.resendVisitor = new ResendRequestVisitor(objectTransport);
    }

    /**
     * Dispatch visitor output message.
     *
     * @param response the response
     * @return the output message
     * @throws FailedExecution        the failed execution
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public OutputMessage dispatchVisitor(Response response) throws
            FailedExecution,
            IOException,
            ClassNotFoundException {
        if ((response instanceof MessageResponse messageResponse)
                || (response instanceof ValidationResponse validationResponse
                        && !validationResponse.isExist())) {
            return response.accept(printVisitor);
        } else {
            return response.accept(resendVisitor);
        }
    }
}
