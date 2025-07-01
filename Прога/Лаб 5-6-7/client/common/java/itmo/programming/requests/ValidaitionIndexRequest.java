package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Validaition index request.
 */
public record ValidaitionIndexRequest(int index, String command, String login) implements
        Request, Serializable {

}
