package itmo.programming.requests;

import itmo.programming.object.HumanBeing;
import java.io.Serializable;

/**
 * The type AddRequest.
 */
public record AddRequest(HumanBeing human, String login) implements Serializable, Request {

    /**
     * The type Message.
     */
    record Message(int id, Request request) {}
}


