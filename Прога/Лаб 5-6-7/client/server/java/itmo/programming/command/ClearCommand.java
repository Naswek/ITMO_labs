package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.requests.ClearRequest;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.storage.ManageDatabase;
import itmo.programming.util.ManageCollection;

/**
 * Класс команды, реализующий очистку всех элементов из коллекции.
 * Наследуется от интерфейса {@link Command} и предоставляет функциональность
 * для полного удаления содержимого коллекции, управляемой объектом {@link ManageCollection}.
 */
public class ClearCommand implements Command {

    /**
     * Объект для управления коллекцией, из которой будут удалены все элементы.
     */
    private final ManageCollection manageCollection;
    private final ManageDatabase manageDatabase;

    /**
     * Конструктор класса, инициализирующий команду с объектом управления коллекцией.
     *
     * @param manageCollection объект типа {@link ManageCollection}, я работы с коллекцией
     * @param manageDatabase   the manage database
     */
    public ClearCommand(ManageCollection manageCollection, ManageDatabase manageDatabase) {
        this.manageCollection = manageCollection;
        this.manageDatabase = manageDatabase;
    }

    @Override
    public String getDescription() {
        return getCommandName() + " - команда, очищающая всю коллекцию";
    }

    @Override
    public MessageResponse execute(Request request) {

        final var clearRequest = (ClearRequest) request;

        final int id = manageDatabase.getUserId(clearRequest.login());
        manageCollection.clear(id);
        manageDatabase.clear(clearRequest.login());
        return new MessageResponse("Коллекция успешно очищена!");
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("clear");
    }
}
