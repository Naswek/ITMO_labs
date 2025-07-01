package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.util.Initialization;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Класс команды, реализующий вывод списка всех доступных команд с их описаниями.
 * Наследуется от интерфейса {@link Command} и предоставляет пользователю помощь
 * по доступным командам, хранящимся в коллекции
 */
public class HelpCommand implements Command {


    private final Initialization initialization;

    /**
     * Конструктор класса, создающий экземпляр команды без параметров.
     * Не требует дополнительных зависимостей для выполнения.
     *
     * @param initialization the initialization
     */
    public HelpCommand(Initialization initialization) {
        this.initialization = initialization;
    }

    @Override
    public String getDescription() {
        return getCommandName() + " - команда, выводящая список доступных команд";
    }

    @Override
    public MessageResponse execute(Request request) {
        final HashMap<Class<?>, Command> commands = initialization.getCommands();
        final String commandDescriptions = commands.values().stream()
                .map(Command::getDescription)
                .collect(Collectors.joining("\n"));
        return new MessageResponse(commandDescriptions);
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("help");
    }
}
