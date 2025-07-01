package itmo.programming.creator.fields;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.exceptions.InvalidScriptInputException;
import itmo.programming.input.InputSource;
import itmo.programming.output.console.ConsoleOutput;

/**
 * The type Soundtrack.
 */
public class Soundtrack {

    private final InputSource inputSource;

    private final ConsoleOutput printer;

    /**
     * Instantiates a new Soundtrack.
     *
     * @param inputSource the input source
     * @param printer     the printer
     */
    public Soundtrack(InputSource inputSource, ConsoleOutput printer) {
        this.inputSource = inputSource;
        this.printer = printer;
    }

    /**
     * Create soundtrack field string.
     *
     * @return the string
     */
    public String createSoundtrackField() {
        while (true) {
            try {
                printer.print(PrettyColor.yellow("Введите имя саундтрек: "));
                return getValidatedSoundtrackField();

            } catch (InvalidInputException e) {
                printer.print(PrettyColor.red("Ошибка ввода, попробуйте снова"));
            }
        }
    }

    /**
     * Create soundtrack from script string.
     *
     * @return the string
     * @throws InvalidScriptInputException the invalid script input exception
     */
    public String createSoundtrackFromScript() throws InvalidScriptInputException {
        return getValidatedSoundtrackField();
    }

    private String getValidatedSoundtrackField() {
        final String soundtrack = inputSource.getTextFromUser().orElse(" ");

        if (!soundtrack.isEmpty()) {
            return soundtrack;
        }
        throw new InvalidInputException(PrettyColor.red("Имя саунда не может быть пустым"));
    }
}
