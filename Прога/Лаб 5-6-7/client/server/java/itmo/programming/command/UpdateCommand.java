package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.OwnershipException;
import itmo.programming.object.HumanBeing;
import itmo.programming.requests.Request;
import itmo.programming.requests.UpdateRequest;
import itmo.programming.responses.MessageResponse;
import itmo.programming.storage.ManageDatabase;
import itmo.programming.util.ManageCollection;
import itmo.programming.util.Validation;

/**
 * Класс команды, реализующий обновление информации об объекте в коллекции
 * по заданному идентификатору. Наследуется от интерфейса {@link Command}
 * и предоставляет функциональность для замены объекта {@link HumanBeing}
 * в коллекции, управляемой объектом {@link ManageCollection},
 * на основе переданного ID.
 */
public class UpdateCommand implements Command {

    /**
     * Объект для управления коллекцией, в которой обновляется элемент.
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
     * @param validation       объект для проверки вводимых данных
     * @param manageDatabase   the manage database
     */
    public UpdateCommand(
            ManageCollection manageCollection,
            Validation validation,
            ManageDatabase manageDatabase) {
        this.manageCollection = manageCollection;
        this.validation = validation;
        this.manageDatabase = manageDatabase;
    }

    @Override
    public String getDescription() {
        return getCommandName() + " - обновляет информацию о существующем объекте";
    }

    @Override
    public MessageResponse execute(Request request) {

        final UpdateRequest updateRequest = (UpdateRequest) request;

        final int id = updateRequest.id();
        final HumanBeing newHuman = updateRequest.human();

        final HumanBeing oldHuman = manageCollection.getHumanBeingById(id);

        if (oldHuman != null && validation.validateHuman(newHuman)) {
            try {
                final HumanBeing human = manageDatabase.update(newHuman, id, updateRequest.login());
                if (human == null) {
                    return new MessageResponse("Произошла ошибка");
                } else {
                    manageCollection.update(human, id);
                }
            } catch (OwnershipException e) {
                return new MessageResponse(e.getMessage());
            }
            return new MessageResponse("Объект успешно обновлен!");
        } else {
            return new MessageResponse("Объект с таким index не найден :(");
        }
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("updateById index");
    }
}
