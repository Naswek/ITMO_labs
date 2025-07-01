package itmo.programming.creator.fields;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.exceptions.InvalidScriptInputException;
import itmo.programming.input.InputSource;
import itmo.programming.output.console.ConsoleOutput;

/**
 * The type Name person.
 */
public class NamePerson {

    private final InputSource inputSource;

    private final ConsoleOutput printer;

    /**
     * Instantiates a new Name person.
     *
     * @param inputSource the input source
     * @param printer     the printer
     */
    public NamePerson(InputSource inputSource, ConsoleOutput printer) {
        this.inputSource = inputSource;
        this.printer = printer;
    }

    /**
     * Create name field string.
     *
     * @return the string
     */
    public String createNameField() {
        while (true) {
            try {
                printer.print(PrettyColor.yellow("Введите имя: "));
                final String name = inputSource.getTextFromUser().orElse("");

                if (!name.isEmpty()) {
                    return name;
                }

                printer.print(PrettyColor.red("Имя персоны не может быть пустым, "
                        + "введите имя повторно"));
            } catch (InvalidInputException e) {
                printer.print("Ошибка ввода, попробуйте снова");
            }
        }
    }

    /**
     * Create name from script string.
     *
     * @return the string
     */
    public String createNameFromScript() {
        try {
            return inputSource.getTextFromUser().orElse("");
        } catch (InvalidInputException e) {
            throw new InvalidScriptInputException("Невалидные данные для поля name");
        }
    }
}
