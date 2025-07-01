package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.exceptions.OwnershipException;
import itmo.programming.requests.RemoveFirstRequest;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.storage.ManageDatabase;
import itmo.programming.util.ManageCollection;

/**
 * Класс команды, реализующий удаление первого непустого элемента из коллекции.
 * Наследуется от интерфейса {@link Command} и предоставляет функциональность
 * для удаления первого доступного элемента из коллекции, управляемой объектом
 * {@link ManageCollection}.
 */
public class RemoveFirstCommand implements Command {

    /**
     * Объект для управления коллекцией, из которой удаляется первый элемент.
     */
    private final ManageCollection manageCollection;
    private final ManageDatabase manageDatabase;

    /**
     * Конструктор класса, инициализирующий команду с объектом управления коллекцией.
     *
     * @param manageCollection объект типа {@link ManageCollection}, используемый
     * @param manageDatabase   the manage database
     */
    public RemoveFirstCommand(ManageCollection manageCollection, ManageDatabase manageDatabase) {
        this.manageCollection = manageCollection;
        this.manageDatabase = manageDatabase;
    }

    @Override
    public String getDescription() {
        return getCommandName() + " - команда, удаляющая первый элемент в коллекции";
    }

    @Override
    public MessageResponse execute(Request request) {

        final var removeFirstRequest = (RemoveFirstRequest) request;

        try {
            try {
                final int id = manageCollection.getId(0);

                boolean flag = manageDatabase.removeById(id, removeFirstRequest.login());
                if (flag) {
                    manageCollection.removeFirstHumanBeing();
                } else {
                    return new MessageResponse("Произошла ошибка");
                }
            } catch (OwnershipException e) {
                return new MessageResponse(e.getMessage());

            }
            return new MessageResponse("Объект успешно удалён!");
        } catch (InvalidInputException e) {
            return new MessageResponse(e.getMessage());
        }
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("remove_first");
    }
}
