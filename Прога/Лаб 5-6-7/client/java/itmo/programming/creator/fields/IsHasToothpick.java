package itmo.programming.creator.fields;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.exceptions.InvalidScriptInputException;
import itmo.programming.input.InputSource;
import itmo.programming.output.console.ConsoleOutput;

/**
 * The type Is has toothpick.
 */
public class IsHasToothpick {

    private final String[] booleanValues = {"True", "False"};

    private final InputSource inputSource;

    private final Validation validation;

    private final ConsoleOutput printer;

    /**
     * Instantiates a new Is has toothpick.
     *
     * @param inputSource the input source
     * @param validation  the validation
     * @param printer     the printer
     */
    public IsHasToothpick(InputSource inputSource, Validation validation, ConsoleOutput printer) {
        this.inputSource = inputSource;
        this.validation = validation;
        this.printer = printer;
    }

    /**
     * Create is toothpick field boolean.
     *
     * @return the boolean
     */
    public boolean createIsToothpickField() {
        while (true) {
            try {
                printer.print(PrettyColor.yellow("Есть у персонажа зубочистка: "));
                for (int i = 0; i < booleanValues.length; i++) {
                    printer.print((i + 1) + ") " + booleanValues[i]);
                }

                final int userInput = inputSource.getIntFromUser().orElse(0);

                if (validation.isValidInputForBoolean(userInput)) {
                    return parseIsHasToothpickToBoolean(userInput);
                } else {
                    printer.print(PrettyColor.red("Напишите цифру в пределах допустимых"));
                }
            } catch (InvalidInputException e) {
                printer.print("Ошибка ввода, напишите цифру");
            }
        }
    }

    /**
     * Create is has toothpick from script boolean.
     *
     * @return the boolean
     */
    public boolean createIsHasToothpickFromScript() {
        final int userInput = inputSource.getIntFromUser().orElse(0);

        if (validation.isValidInputForBoolean(userInput)) {
            return parseIsHasToothpickToBoolean(userInput);
        }
        throw new InvalidScriptInputException("Невалидные данные для поля isHasToothpick");
    }

    /**
     * Parse is has toothpick to boolean boolean.
     *
     * @param userInput the user input
     * @return the boolean
     */
    public boolean parseIsHasToothpickToBoolean(int userInput) {
        return Boolean.parseBoolean(
                booleanValues[userInput - 1]);
    }
}
