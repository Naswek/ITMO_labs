package itmo.programming.creator.fields;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.input.InputSource;
import itmo.programming.object.Mood;
import itmo.programming.output.console.ConsoleOutput;


/**
 * The type Mood create.
 */
public class MoodCreate {

    private final ConsoleOutput printer;

    private final InputSource inputSource;

    private final Validation validation;

    /**
     * Instantiates a new Mood create.
     *
     * @param printer     the printer
     * @param inputSource the input source
     * @param validation  the validation
     */
    public MoodCreate(ConsoleOutput printer, InputSource inputSource, Validation validation) {
        this.printer = printer;
        this.inputSource = inputSource;
        this.validation = validation;
    }

    /**
     * Create mood field mood.
     *
     * @return the mood
     */
    public Mood createMoodField() {
        while (true) {
            try {
                printer.print(PrettyColor.yellow("Выберите настроение: "));
                for (int i = 0; i < Mood.values().length; i++) {
                    printer.print(i + 1 + ") " + Mood.values()[i]);
                }

                return getValidatedMood();
            } catch (InvalidInputException e) {
                printer.print("Ошибка ввода, напишите цифру");
            }
        }
    }

    /**
     * Create mood field from script mood.
     *
     * @return the mood
     */
    public Mood createMoodFieldFromScript() {
        return getValidatedMood();
    }

    private Mood getValidatedMood() {
        final String userInput = inputSource.getTextFromUser().orElse("");

        if (Mood.contains(userInput)) {
            return Mood.getMood(userInput);
        }


        if (validation.isInputForEnumIsInt(userInput)) {

            final int numOfMood = Integer.parseInt(userInput);

            if (validation.isValidInputForMood(numOfMood)) {
                return Mood.values()[numOfMood - 1];
            }
        }

        throw new InvalidInputException("Невалидные данные для поля Mood");
    }
}
