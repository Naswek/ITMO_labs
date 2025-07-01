package itmo.programming.responses;

import itmo.programming.message.OutputMessage;
import java.io.Serializable;

/**
 * The type Response.
 */
public record MessageResponse(String message) implements Serializable, Response {
    /**
     * Instantiates a new Response.
     *
     * @param message the message
     */
    public MessageResponse(Object message) {
        this(message.toString());
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public OutputMessage accept(ResponseVisitor visitor) {
        return visitor.visit(this);
    }
}
