package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type User objects request.
 */
public record UserObjectsRequest(String login) implements Serializable, Request {

}
