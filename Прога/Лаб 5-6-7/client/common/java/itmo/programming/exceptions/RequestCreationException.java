package itmo.programming.exceptions;

/**
 * The type AddRequest creation exception.
 */
public class RequestCreationException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением и причиной.
     *
     * @param message детальное сообщение об ошибке
     * @param cause   причина исключения
     */
    public RequestCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public RequestCreationException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение с указанной причиной.
     *
     * @param cause причина исключения
     */
    public RequestCreationException(Throwable cause) {
        super(cause);
    }
}
