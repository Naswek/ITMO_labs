package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.object.HumanBeing;
import itmo.programming.requests.CountLessThanSoundtrackNameRequest;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.util.ManageCollection;


/**
 * Класс команды, реализующий подсчет элементов коллекции,
 * у которых длина названия саундтрека меньше заданного значения.
 * Наследуется от интерфейса {@link Command} и используется для анализа объектов {@link HumanBeing}
 * в коллекции, управляемой объектом {@link ManageCollection}, на основе длины поля soundtrackName.
 */
public class CountLessThanSoundtrackNameCommand implements Command {

    /**
     * Объект для управления коллекцией, из которой берутся элементы для подсчета.
     */
    private final ManageCollection manageCollection;

    /**
     * Конструктор класса, инициализирующий команду с объектом управления коллекцией.
     *
     * @param manageCollection объект типа {@link ManageCollection}, содержащий
     */
    public CountLessThanSoundtrackNameCommand(
            ManageCollection manageCollection) {
        this.manageCollection = manageCollection;
    }

    @Override
    public String getDescription() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getCommandName());
        builder.append(" - команда, выводящая кол-во элементов в коллекции, ");
        builder.append("у которых длина soundtrackName меньше заданного");
        return builder.toString();
    }

    @Override
    public MessageResponse execute(Request request) {
        final CountLessThanSoundtrackNameRequest countLessThanSoundtrackNameRequest
                = (CountLessThanSoundtrackNameRequest) request;
        return new MessageResponse(manageCollection.countLessThanSoundtrackName(
                countLessThanSoundtrackNameRequest.word()));
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("count_less_than_soundtrack_name soundtrackName");
    }
}
