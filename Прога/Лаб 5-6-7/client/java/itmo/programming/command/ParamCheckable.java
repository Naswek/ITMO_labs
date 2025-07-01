package itmo.programming.command;

import itmo.programming.exceptions.FailedExecution;
import itmo.programming.requests.Request;
import itmo.programming.responses.ValidationResponse;

/**
 * The interface Param checkable.
 */
public interface ParamCheckable {

    /**
     * Check parameter request.
     *
     * @param response the response
     * @return the request
     * @throws FailedExecution the failed execution
     */
    Request checkParameter(ValidationResponse response) throws FailedExecution;

    /**
     * Is parameter valid boolean.
     *
     * @param parameter the parameter
     * @return the boolean
     */
    boolean isParameterValid(String parameter);

}
