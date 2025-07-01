package itmo.programming.creator.fields;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.exceptions.InvalidScriptInputException;
import itmo.programming.input.InputSource;
import itmo.programming.output.console.ConsoleOutput;

/**
 * The type Is hero.
 */
public class IsHero {

    private final String[] booleanValues = {"True", "False"};

    private final InputSource inputSource;

    private final Validation validation;

    private final ConsoleOutput printer;

    /**
     * Instantiates a new Is hero.
     *
     * @param inputSource the input source
     * @param validation  the validation
     * @param printer     the printer
     */
    public IsHero(InputSource inputSource, Validation validation, ConsoleOutput printer) {
        this.inputSource = inputSource;
        this.validation = validation;
        this.printer = printer;
    }

    /**
     * Create is hero field boolean.
     *
     * @return the boolean
     */
    public boolean createIsHeroField() {
        while (true) {
            try {
                printer.print(PrettyColor.yellow("Введите является ли персонаж героем: "));
                for (int i = 0; i < booleanValues.length; i++) {
                    printer.print((i + 1) + ") " + booleanValues[i]);
                }

                final int userInput = inputSource.getIntFromUser().orElse(0);

                if (validation.isValidInputForBoolean(userInput)) {
                    return parseIsHeroToBoolean(userInput);
                } else {
                    printer.print(PrettyColor.red("Напишите цифру в пределах допустимых"));
                }
            } catch (InvalidInputException e) {
                printer.print("Ошибка ввода, напишите цифру");
            }
        }
    }

    /**
     * Create is hero field from script boolean.
     *
     * @return the boolean
     */
    public boolean createIsHeroFieldFromScript() {
        final int userInput = inputSource.getIntFromUser().orElse(0);

        if (validation.isValidInputForBoolean(userInput)) {
            return parseIsHeroToBoolean(userInput);
        }
        throw new InvalidScriptInputException("Невалидные данные для поля isHero");
    }

    private boolean parseIsHeroToBoolean(int userInput) {
        return Boolean.parseBoolean(
                booleanValues[userInput - 1]);
    }
}
