package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Remove by index request.
 */
public record RemoveByIndexRequest(int index, String login) implements Serializable, Request {

}

