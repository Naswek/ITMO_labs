package itmo.programming.exceptions;


/**
 * Исключение, связанное с работой консоли.
 * Используется для обработки ошибок ввода/вывода и других проблем,
 * возникающих при взаимодействии с консольным интерфейсом.
 */
public class ConsoleException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением и причиной.
     *
     * @param message детальное сообщение об ошибке
     * @param cause   причина исключения
     */
    public ConsoleException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public ConsoleException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение с указанной причиной.
     *
     * @param cause причина исключения
     */
    public ConsoleException(Throwable cause) {
        super(cause);
    }
}
