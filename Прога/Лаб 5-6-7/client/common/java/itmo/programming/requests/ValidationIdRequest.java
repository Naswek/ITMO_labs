package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Validation index request.
 */
public record ValidationIdRequest(int id, String command, String login) implements
        Request, Serializable {

}
