package itmo.programming.command;

import itmo.programming.exceptions.FailedExecution;
import itmo.programming.requests.Request;
import java.io.IOException;

/**
 * The interface Promptable.
 */
public interface Promptable {

    /**
     * Create request request.
     *
     * @param parameter the parameter
     * @param login     the login
     * @return the request
     * @throws FailedExecution the failed execution
     * @throws IOException     the io exception
     */
    Request createRequest(String parameter, String login) throws FailedExecution, IOException;
}
