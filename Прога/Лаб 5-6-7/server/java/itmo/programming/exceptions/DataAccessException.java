package itmo.programming.exceptions;

/**
 * The type Data access exception.
 */
public class DataAccessException extends RuntimeException   {

    /**
     * Instantiates a new Data access exception.
     *
     * @param message the message
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
