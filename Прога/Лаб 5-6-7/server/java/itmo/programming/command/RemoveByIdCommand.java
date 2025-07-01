package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.OwnershipException;
import itmo.programming.object.HumanBeing;
import itmo.programming.requests.RemoveByIdRequest;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.storage.ManageDatabase;
import itmo.programming.util.ManageCollection;
import itmo.programming.util.Validation;

/**
 * Класс команды, реализующий удаление элемента из коллекции по заданному идентификатору.
 * Наследуется от интерфейса {@link Command} и предоставляет функциональность
 * для удаления объекта {@link HumanBeing} из коллекции, управляемой
 * объектом {@link ManageCollection}, на основе переданного ID.
 */
public class RemoveByIdCommand implements Command {

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
     * @param manageCollection объект типа {@link ManageCollection}, используемый с коллекцией
     * @param validation       объект для проверки ввода
     * @param manageDatabase   the manage database
     */
    public RemoveByIdCommand(
            ManageCollection manageCollection,
            Validation validation,
            ManageDatabase manageDatabase) {
        this.manageCollection = manageCollection;
        this.validation = validation;
        this.manageDatabase = manageDatabase;
    }

    @Override
    public String getDescription() {
        return getCommandName() + " - команда, убирающая элемент по айди";
    }

    @Override
    public MessageResponse execute(Request request) {

        final RemoveByIdRequest removeByIdRequest = (RemoveByIdRequest) request;

        final int id = removeByIdRequest.id();
        try {
            boolean flag = manageDatabase.removeById(id, removeByIdRequest.login());
            if (flag) {
                manageCollection.removeById(id);
            } else {
                return new MessageResponse("Произошла ошибка");
            }
        } catch (OwnershipException e) {
            return new MessageResponse(e.getMessage());
        }
        return new MessageResponse("Объект удален");

    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("remove_by_id id");
    }
}
