package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.object.HumanBeing;
import itmo.programming.requests.CountGreaterThanCarRequest;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.util.ManageCollection;

/**
 * Класс команды, реализующий подсчет элементов коллекции,
 * у которых значение поля car превышает заданное.
 * Наследуется от интерфейса {@link Command} и используется для анализа объектов {@link HumanBeing}
 * в коллекции, управляемой объектом {@link ManageCollection}, на основе длины названия их машины.
 */
public class CountGreaterThanCarCommand implements Command {


    /**
     * Объект для управления коллекцией, из которой берутся элементы для подсчета.
     */
    private final ManageCollection manageCollection;

    /**
     * Конструктор класса, инициализирующий команду с объектом управления коллекцией.
     *
     * @param manageCollection объект типа {@link ManageCollection}, содержащий  анализа
     */
    public CountGreaterThanCarCommand(ManageCollection manageCollection) {
        this.manageCollection = manageCollection;
    }

    @Override
    public String getDescription() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getCommandName());
        builder.append(" - выводит кол-во элементов, ");
        builder.append("значение поля car которых больше заданного");
        return builder.toString();
    }

    @Override
    public MessageResponse execute(Request request) {
        final CountGreaterThanCarRequest countGreaterThanCarCommandRequest
                = (CountGreaterThanCarRequest) request; //это я переделаю,
        // тк такой тайп каст не очень
        return new MessageResponse(manageCollection.countGreaterThanCar(
                countGreaterThanCarCommandRequest.word()));
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("count_greater_than_car car");
    }
}
