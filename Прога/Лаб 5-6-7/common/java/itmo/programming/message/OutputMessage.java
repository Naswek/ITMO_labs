package itmo.programming.message;

/**
 * The type Output message.
 */
public record OutputMessage(String message, boolean flag, String login) {
    /**
     * Instantiates a new Output message.
     *
     * @param message the message
     */
    public OutputMessage(String message) {
        this(message, false, null);
    }
}
