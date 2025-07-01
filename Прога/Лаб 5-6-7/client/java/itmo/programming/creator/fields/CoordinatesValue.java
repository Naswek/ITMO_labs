package itmo.programming.creator.fields;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.exceptions.InvalidScriptInputException;
import itmo.programming.input.InputSource;
import itmo.programming.object.Coordinates;
import itmo.programming.output.console.ConsoleOutput;

/**
 * The type Coordinates value.
 */
public class CoordinatesValue {

    private final InputSource inputSource;

    private final Validation validation;

    private final ConsoleOutput printer;

    /**
     * Instantiates a new Coordinates value.
     *
     * @param inputSource    the input source
     * @param validation     the validation
     * @param consolePrinter the console printer
     */
    public CoordinatesValue(
            InputSource inputSource,
            Validation validation,
            ConsoleOutput consolePrinter) {
        this.inputSource = inputSource;
        this.validation = validation;
        this.printer = consolePrinter;
    }

    /**
     * Create coordinates field coordinates.
     *
     * @return the coordinates
     */
    public Coordinates createCoordinatesField() {
        try {
            return new Coordinates(createX(), createY());
        } catch (InvalidInputException e) {
            throw new InvalidInputException(PrettyColor.red("Ошибка ввода, напишите числа и "
                    + "проверьте, чтобы число удовлетворяло условиям"));
        }
    }

    /**
     * Create coordinates from script coordinates.
     *
     * @return the coordinates
     */
    public Coordinates createCoordinatesFromScript() {
        try {
            final long x1Value = inputSource.getLongFromUser().orElse(0L);
            final float y1Value = inputSource.getFloatFromUser().orElse(0f);

            if (validation.isValidX(x1Value) && validation.isValidY(y1Value)) {
                return new Coordinates(x1Value, y1Value);
            }

            throw new InvalidScriptInputException("Невалидные данные для поля Coordinates");
        } catch (InvalidInputException e) {
            throw new InvalidScriptInputException("Невалидные данные для поля Coordinates");

        }
    }

    private long createX() {
        while (true) {
            printer.print(PrettyColor.yellow("Введите координаты x (>-449): "));
            final long x1Value = inputSource.getLongFromUser().orElse(0L);

            if (validation.isValidX(x1Value)) {
                return x1Value;
            } else {
                printer.print(PrettyColor.red("Введите число, "
                        + "удовлетворяющее условиям: "));
            }
        }
    }

    private Float createY() {
        while (true) {
            printer.print(PrettyColor.yellow("Введите координаты y (< 673 && != 0): "));
            final float y1Value = inputSource.getFloatFromUser().orElse(0f);

            if (validation.isValidY(y1Value)) {
                return y1Value;
            } else {
                printer.print(PrettyColor.red("Введите число, "
                        + "удовлетворяющее условиям: "));
            }
        }
    }
}
