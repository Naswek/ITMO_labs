package itmo.programming.exceptions;

/**
 * The type Invalid script input exception.
 */
public class InvalidScriptInputException extends RuntimeException {
    /**
     * Создает новое исключение с указанным сообщением и причиной.
     *
     * @param message детальное сообщение об ошибке
     * @param cause   причина исключения
     */
    public InvalidScriptInputException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public InvalidScriptInputException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение с указанной причиной.
     *
     * @param cause причина исключения
     */
    public InvalidScriptInputException(Throwable cause) {
        super(cause);
    }
}
