package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Clear request.
 */
public record ClearRequest(String login) implements Serializable, Request {
    /**
     * The type Message.
     */
    record Message(int id, Request request) {}
}
