package itmo.programming.exceptions;

/**
 * The type Failed parameter validation exception.
 */
public class FailedParameterValidationException extends RuntimeException {
    /**
     * Создает исключение с указанным сообщением об ошибке и причиной.
     *
     * @param message детальное сообщение об ошибке
     * @param cause   исключение, вызвавшее данную ошибку
     */
    public FailedParameterValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает исключение с указанным сообщением об ошибке.
     *
     * @param message детальное сообщение об ошибке
     */
    public FailedParameterValidationException(String message) {
        super(message);
    }

    /**
     * Создает исключение с указанной причиной.
     *
     * @param cause исключение, вызвавшее данную ошибку
     */
    public FailedParameterValidationException(Throwable cause) {
        super(cause);
    }
}
