package itmo.programming.util;

import itmo.programming.command.Command;
import itmo.programming.command.validation.CheckParameter;
import itmo.programming.command.validation.CheckUser;
import itmo.programming.exceptions.DataAccessException;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.responses.Response;
import java.io.IOException;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для управления командами и их выполнения в системе.
 * Обеспечивает обработку пользовательского ввода, выполнение соответствующих команд
 * и взаимодействие с объектами {@link Initialization} и {@link Command}.
 */
public class ManageCommand {

    private final Logger logger = LogManager.getLogger(ManageCommand.class);

    private final Initialization initialization;

    /**
     * Конструктор класса.
     * Инициализирует объект с заданным экземпляром {@link Initialization}.
     *
     * @param initialization объект {@link Initialization} для доступа к командам
     */
    public ManageCommand(Initialization initialization) {
        this.initialization = initialization;
    }

    /**
     * Выполняет команду на основе введённого текста.
     * Получает карту команд из {@link Initialization#getCommands()},
     * извлекает команду и параметр из текста,
     * вызывает метод для соответствующей команды.
     * Обрабатывает ошибки, связанные с неизвестными командами или вводом-выводом.
     *
     * @param request строка ввода пользователя, содержащая команду и, возможно, параметр
     * @return the response
     * @throws IOException the io exception
     */
    public Response executeCommand(Request request) throws IOException {
        try {
            final HashMap<Class<?>, Command> commands = initialization.getCommands();
            final HashMap<Class<?>, CheckParameter> checks = initialization.getChecks();
            final HashMap<Class<?>, CheckUser> authenticationsChecks
                    = initialization.getAuthenticationsChecks();

            if (commands.containsKey(request.getClass())) {
                final Command command = commands.get(request.getClass());
                return command.execute(request);
            }
            if (checks.containsKey(request.getClass())) {
                final CheckParameter checkParameter = checks.get(request.getClass());
                return checkParameter.checkParameter(request);
            } else {
                System.out.println("Проверка пользователя: " + request.getClass());
                final CheckUser checkUser = authenticationsChecks.get(request.getClass());
                return checkUser.checkUser(request);
            }

        } catch (FailedExecution e) {
            logger.error("Ошибка при интерпретации реквеста {}", request.getClass());
            return new MessageResponse("Не удалось выполнить реквест: " + request.getClass());
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Ошибка запроса базы данных: {}", e.getMessage());
            return new MessageResponse("Ошибка выполнения команды");
        }
    }
}
