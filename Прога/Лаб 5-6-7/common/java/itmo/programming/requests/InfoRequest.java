package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Info request.
 */
public record InfoRequest(String login) implements Serializable, Request {
}
