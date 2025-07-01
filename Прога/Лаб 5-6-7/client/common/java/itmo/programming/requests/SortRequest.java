package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Sort request.
 */
public record SortRequest(String login) implements Serializable, Request {

}
