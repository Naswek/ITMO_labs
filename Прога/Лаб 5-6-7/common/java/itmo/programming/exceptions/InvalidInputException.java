package itmo.programming.exceptions;

/**
 * The type Invalid input exception.
 */
public class InvalidInputException extends RuntimeException {
    /**
     * Создает новое исключение с указанным сообщением и причиной.
     *
     * @param message детальное сообщение об ошибке
     * @param cause   причина исключения
     */
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public InvalidInputException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение с указанной причиной.
     *
     * @param cause причина исключения
     */
    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
