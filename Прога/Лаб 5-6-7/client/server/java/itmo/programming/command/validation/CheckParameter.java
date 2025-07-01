package itmo.programming.command.validation;

import itmo.programming.requests.Request;
import itmo.programming.responses.ValidationResponse;

/**
 * The interface Check parameter.
 */
public interface CheckParameter {
    /**
     * Check parameter validation response.
     *
     * @param request the request
     * @return the validation response
     */
    ValidationResponse checkParameter(Request request);
}
