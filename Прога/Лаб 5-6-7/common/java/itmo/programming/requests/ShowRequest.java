package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Show request.
 */
public record ShowRequest(String login) implements Serializable, Request {
}
