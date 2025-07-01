package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.util.ManageCollection;


/**
 * Класс команды, реализующий отображение всех элементов коллекции.
 * Наследуется от интерфейса {@link Command} и предоставляет функциональность
 * для вывода списка объектов, хранящихся в коллекции, управляемой объектом
 * {@link ManageCollection}.
 */
public class ShowCommand implements Command {

    /**
     * Объект для управления коллекцией, элементы которой будут отображены.
     */
    private final ManageCollection manageCollection;

    /**
     * Конструктор класса, инициализирующий команду с объектом управления коллекцией.
     *
     * @param manageCollection объект типа {@link ManageCollection}, содержащий
     */
    public ShowCommand(ManageCollection manageCollection) {
        this.manageCollection = manageCollection;
    }

    @Override
    public String getDescription() {
        return getCommandName() + " - команда, показывающая список объектов в коллекции";
    }

    @Override
    public MessageResponse execute(Request request) {
        return new MessageResponse(manageCollection.showAllHumans());
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("show");
    }
}
