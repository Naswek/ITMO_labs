package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Remove by index request.
 */
public record RemoveByIdRequest(int id, String login) implements Serializable, Request {

}
