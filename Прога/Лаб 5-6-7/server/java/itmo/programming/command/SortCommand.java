package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.util.ManageCollection;


/**
 * Класс команды, реализующий сортировку элементов коллекции.
 * Наследуется от интерфейса {@link Command} и предоставляет функциональность
 * для упорядочивания элементов в коллекции, управляемой объектом {@link ManageCollection}.
 */
public class SortCommand implements Command {

    /**
     * Объект для управления коллекцией, которая будет отсортирована.
     */
    private final ManageCollection manageCollection;

    /**
     * Конструктор класса, инициализирующий команду с объектом управления коллекцией.
     *
     * @param manageCollection объект типа {@link ManageCollection},
     */
    public SortCommand(ManageCollection manageCollection) {
        this.manageCollection = manageCollection;
    }

    @Override
    public String getDescription() {
        return getCommandName() + " - сортирует коллекцию по естественному порядку";
    }

    @Override
    public MessageResponse execute(Request request) {
        manageCollection.sort();
        return new MessageResponse("Коллекция отсортирована!");
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("sort");
    }
}
