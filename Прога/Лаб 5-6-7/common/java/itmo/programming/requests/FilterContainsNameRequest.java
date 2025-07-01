package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Filter contains name request.
 */
public record FilterContainsNameRequest(String word, String login) implements
        Serializable, Request {

}
