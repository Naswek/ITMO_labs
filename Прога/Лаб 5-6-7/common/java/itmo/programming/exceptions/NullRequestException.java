package itmo.programming.exceptions;

/**
 * The type Null request exception.
 */
public class NullRequestException extends RuntimeException {
    /**
     * Создает исключение с указанным сообщением об ошибке и причиной.
     *
     * @param message детальное сообщение об ошибке
     * @param cause   исключение, вызвавшее данную ошибку
     */
    public NullRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает исключение с указанным сообщением об ошибке.
     *
     * @param message детальное сообщение об ошибке
     */
    public NullRequestException(String message) {
        super(message);
    }

    /**
     * Создает исключение с указанной причиной.
     *
     * @param cause исключение, вызвавшее данную ошибку
     */
    public NullRequestException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Null request exception.
     */
    public NullRequestException() {

    }

}
