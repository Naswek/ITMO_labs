package itmo.programming.command;

import itmo.programming.ParseLocalDate;
import itmo.programming.PrettyColor;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.util.Initialization;
import itmo.programming.util.ManageCollection;
import java.io.IOException;

/**
 * Класс команды, реализующий вывод информации о коллекции.
 * Наследуется от интерфейса {@link Command} и предоставляет данные о размере и дате инициализации
 * коллекции, управляемой объектом {@link ManageCollection}.
 */
public class InfoCommand implements Command {

    /**
     * Объект для управления коллекцией, о которой предоставляется информация.
     */
    private final ManageCollection manageCollection;
    private final Initialization initialization;

    /**
     * Конструктор класса, инициализирующий команду с объектом управления коллекцией.
     *
     * @param manageCollection объект типа {@link ManageCollection}, содержащий данные о коллекции
     * @param initialization   the initialization
     */
    public InfoCommand(
            ManageCollection manageCollection,
            Initialization initialization) {
        this.manageCollection = manageCollection;
        this.initialization = initialization;
    }

    @Override
    public String getDescription() {
        return getCommandName() + " - команда, выводящая информацию о коллекции";
    }

    @Override
    public MessageResponse execute(Request request) throws FailedExecution, IOException {
        return new MessageResponse("Размер коллекции: " + manageCollection.size() + "\n"
                + "Дата инициализации: "
                + ParseLocalDate.parseLocalDateToString(initialization.getInitializationDate()));
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("info");
    }
}
