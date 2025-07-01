package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.requests.FilterContainsNameRequest;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.util.ManageCollection;


/**
 * Класс команды, реализующий фильтрацию элементов коллекции по наличию подстроки в поле name.
 * Наследуется от интерфейса {@link Command} и используется для вывода элементов из коллекции,
 * управляемой объектом {@link ManageCollection}, у которых поле name содержит заданную
 * подстроку.
 */
public class FilterContainsNameCommand implements Command {

    /**
     * Объект для управления коллекцией, из которой фильтруются элементы.
     */
    private final ManageCollection manageCollection;

    /**
     * Конструктор класса, инициализирующий команду с объектом управления коллекцией.
     *
     * @param manageCollection объект типа {@link ManageCollection}, содержащийя фильтрации
     */
    public FilterContainsNameCommand(ManageCollection manageCollection) {
        this.manageCollection = manageCollection;
    }


    @Override
    public String getDescription() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getCommandName());
        builder.append(" - команда, выводящая элементы, ");
        builder.append("значение поля name которых ");
        builder.append("содержит заданную подстроку");
        return builder.toString();
    }

    @Override
    public MessageResponse execute(Request request) {
        final FilterContainsNameRequest filterContainsNameRequest =
                (FilterContainsNameRequest) request;
        final StringBuilder builder = manageCollection.filterContainsName(
                filterContainsNameRequest.word());
        if (builder.length() == 1) {
            return new MessageResponse("Нет имен с такой подстрокой(");
        } else {
            return new MessageResponse(builder.toString());
        }
    }

    @Override
    public String getCommandName() {
        return PrettyColor.yellow("filter_contains_name name");
    }
}
