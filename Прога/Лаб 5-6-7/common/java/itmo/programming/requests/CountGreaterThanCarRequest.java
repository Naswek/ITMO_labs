package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Count greater than car request.
 */
public record CountGreaterThanCarRequest(String word, String login) implements
        Serializable, Request {

}
