package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Help request.
 */
public record HelpRequest(String login) implements Serializable, Request {
}
