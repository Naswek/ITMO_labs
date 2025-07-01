package itmo.programming.exceptions;

/**
 * The type Initialization exception.
 */
public class InitializationException extends Exception {
    /**
     * Создает исключение с указанным сообщением об ошибке и причиной.
     *
     * @param message детальное сообщение об ошибке
     * @param cause   исключение, вызвавшее данную ошибку
     */
    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает исключение с указанным сообщением об ошибке.
     *
     * @param message детальное сообщение об ошибке
     */
    public InitializationException(String message) {
        super(message);
    }

    /**
     * Создает исключение с указанной причиной.
     *
     * @param cause исключение, вызвавшее данную ошибку
     */
    public InitializationException(Throwable cause) {
        super(cause);
    }
}
