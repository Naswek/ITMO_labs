package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.object.HumanBeing;
import itmo.programming.requests.AddRequest;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.storage.ManageDatabase;
import itmo.programming.util.ManageCollection;
import itmo.programming.util.Validation;

/**
 * Класс команды, реализующий добавление нового элемента в коллекцию.
 * Наследуется от интерфейса {@link Command} и предоставляет функциональность
 * для создания и добавления объекта {@link HumanBeing} в управляемую коллекцию.
 */
public class AddCommand implements Command {

    /**
     * Объект для управления коллекцией, в которую добавляются элементы.
     */
    private final ManageCollection manageCollection;

    private final Validation validation;

    private final ManageDatabase manageDatabase;

    /**
     * Конструктор класса, инициализирующий команду с объектом управления коллекцией.
     *
     * @param manageCollection объект типа {@link ManageCollection}для добавления элементов
     * @param validation       the validation
     * @param manageDatabase   the manage database
     */
    public AddCommand(
            ManageCollection manageCollection,
            Validation validation,
            ManageDatabase manageDatabase) {
        this.manageCollection = manageCollection;
        this.validation = validation;
        this.manageDatabase = manageDatabase;
    }

    @Override
    public String getDescription() {
        return getCommandName() + " - команда, добавляющая новый элемент в коллекцию";
    }

    @Override
    public MessageResponse execute(Request request) {
        final AddRequest addRequest = (AddRequest) request; //это я переделаю,
        // тк такой тайп каст не очень
        final HumanBeing human = addRequest.human();

        if (validation.validateHuman(human)) {
            final HumanBeing thisHuman = manageDatabase.add(human, addRequest.login());
            if (human == null) {
                return new MessageResponse("Произошла ошибка");
            }
            manageCollection.add(thisHuman);
            return new MessageResponse("Персонаж добавлен");
        } else {
            return new MessageResponse("Не удалось добавить персонажа");
        }
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("add");
    }
}
