package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Remove first request.
 */
public record RemoveFirstRequest(String login) implements Serializable, Request {
}
