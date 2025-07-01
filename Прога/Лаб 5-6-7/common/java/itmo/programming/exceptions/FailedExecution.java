package itmo.programming.exceptions;

import java.io.IOException;

/**
 * Исключение, выбрасываемое при неудачном выполнении команды.
 * Наследуется от {@link IOException} для обработки ошибок ввода-вывода,
 * связанных с выполнением команд.
 */
public class FailedExecution extends Exception {

    /**
     * Создает исключение с указанным сообщением об ошибке и причиной.
     *
     * @param message детальное сообщение об ошибке
     * @param cause   исключение, вызвавшее данную ошибку
     */
    public FailedExecution(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает исключение с указанным сообщением об ошибке.
     *
     * @param message детальное сообщение об ошибке
     */
    public FailedExecution(String message) {
        super(message);
    }

    /**
     * Создает исключение с указанной причиной.
     *
     * @param cause исключение, вызвавшее данную ошибку
     */
    public FailedExecution(Throwable cause) {
        super(cause);
    }
}
