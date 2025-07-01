package itmo.programming.exceptions;

/**
 * The type Exit command exception.
 */
public class ExitCommandException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением и причиной.
     *
     * @param message детальное сообщение об ошибке
     * @param cause   причина исключения
     */
    public ExitCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public ExitCommandException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение с указанной причиной.
     *
     * @param cause причина исключения
     */
    public ExitCommandException(Throwable cause) {
        super(cause);
    }
}
