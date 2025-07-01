package itmo.programming.command;

import itmo.programming.exceptions.FailedExecution;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Интерфейс, определяющий базовую структуру для всех команд в системе.
 * Все конкретные команды должны реализовывать этот интерфейс для обеспечения единообразного
 * выполнения и описания.
 */
public interface Command {

    /**
     * Выполняет основную логику команды с учетом переданных аргументов.
     * Метод является точкой входа для исполнения функциональности команды.
     *
     * @param request строка аргументов, переданных для выполнения команды, может быть пустой
     * @return the response
     * @throws FailedExecution если возникает ошибка ввода-вывода во время выполнения команды
     * @throws IOException     the io exception
     * @throws SQLException    the sql exception
     */
    MessageResponse execute(Request request) throws FailedExecution, IOException;

    /**
     * Возвращает текстовое описание команды, предназначенное для информирования пользователя.
     * Описание обычно включает краткое объяснение назначения команды.
     *
     * @return строка с описанием команды
     */
    String getDescription();

    /**
     * Gets command name.
     *
     * @return the command name
     */
    String getCommandName();
}
