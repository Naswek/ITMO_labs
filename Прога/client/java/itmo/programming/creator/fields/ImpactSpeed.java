package itmo.programming.creator.fields;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.exceptions.InvalidScriptInputException;
import itmo.programming.input.InputSource;
import itmo.programming.output.console.ConsoleOutput;

/**
 * The type Impact speed.
 */
public class ImpactSpeed {

    private final InputSource inputSource;

    private final Validation validation;

    private final ConsoleOutput printer;

    /**
     * Instantiates a new Impact speed.
     *
     * @param inputSource the input source
     * @param validation  the validation
     * @param printer     the printer
     */
    public ImpactSpeed(InputSource inputSource, Validation validation, ConsoleOutput printer) {
        this.inputSource = inputSource;
        this.validation = validation;
        this.printer = printer;
    }

    /**
     * Create impact speed field float.
     *
     * @return the float
     */
    public float createImpactSpeedField() {
        while (true) {
            try {
                printer.print(PrettyColor.yellow("Введите скорость удара (>-442)"));
                final float impactSpeed = inputSource.getFloatFromUser().orElse(0.0f);

                if (validation.isValidImpactSpeed(impactSpeed)) {
                    return impactSpeed;
                }

                printer.print(PrettyColor.red("Введите скорость удара персонажа, "
                        + "значение должно быть больше -442: "));

            } catch (InvalidInputException e) {
                printer.printErr("Ошибка ввода, напишите число, большее -442");
            }
        }
    }

    /**
     * Create impact speed from script float.
     *
     * @return the float
     * @throws InvalidScriptInputException the invalid script input exception
     */
    public float createImpactSpeedFromScript() throws InvalidScriptInputException {
        final float impactSpeed = inputSource.getFloatFromUser().orElse(0.0f);

        if (validation.isValidImpactSpeed(impactSpeed)) {
            return impactSpeed;
        }

        throw new InvalidScriptInputException("Невалидные данные для поля isHero");
    }
}
