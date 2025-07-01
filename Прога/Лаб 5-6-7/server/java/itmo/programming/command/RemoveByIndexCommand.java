package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.exceptions.OwnershipException;
import itmo.programming.requests.RemoveByIndexRequest;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.storage.ManageDatabase;
import itmo.programming.util.ManageCollection;
import itmo.programming.util.Validation;

/**
 * Класс команды, реализующий удаление элемента из коллекции по заданному индексу.
 * Наследуется от интерфейса {@link Command} и предоставляет функциональность
 * для удаления элемента из коллекции, управляемой объектом {@link ManageCollection},
 * на основе переданного индекса.
 */
public class RemoveByIndexCommand implements Command {

    /**
     * Объект для управления коллекцией, из которой удаляется элемент.
     */
    private final ManageCollection manageCollection;

    /**
     * Объект для проверки вводимых данных.
     */
    private final Validation validation;
    private final ManageDatabase manageDatabase;

    /**
     * Конструктор класса, инициализирующий команду с объектом управления коллекцией.
     *
     * @param manageCollection объект типа {@link ManageCollection}, используемый
     * @param validation       объект для проверки ввода
     * @param manageDatabase   the manage database
     */
    public RemoveByIndexCommand(
            ManageCollection manageCollection,
            Validation validation, ManageDatabase manageDatabase) {
        this.manageCollection = manageCollection;
        this.validation = validation;
        this.manageDatabase = manageDatabase;
    }

    @Override
    public String getDescription() {
        return getCommandName() + "- команда, удаляющая элемент по индексу";
    }

    @Override
    public MessageResponse execute(Request request) throws FailedExecution {

        final RemoveByIndexRequest removeByIndexRequest = (RemoveByIndexRequest) request;

        try {
            int index = removeByIndexRequest.index();
            System.out.println("Index: " + index);
            final int id = manageCollection.getId(removeByIndexRequest.index());
            try {
                boolean flag = manageDatabase.removeById(id, removeByIndexRequest.login());
                if (flag) {
                    manageCollection.removeById(id);
                } else {
                    return new MessageResponse("произошла ошибка");
                }
            } catch (OwnershipException e) {
                return new MessageResponse(e.getMessage());
            }
            return new MessageResponse("Объект успешно удален!");
        } catch (InvalidInputException e) {
            return new MessageResponse(e.getMessage());
        }
    }
        

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("remove_at index");
    }
}
