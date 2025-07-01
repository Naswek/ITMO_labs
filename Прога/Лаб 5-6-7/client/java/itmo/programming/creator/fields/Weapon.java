package itmo.programming.creator.fields;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.input.InputSource;
import itmo.programming.object.WeaponType;
import itmo.programming.output.console.ConsoleOutput;

/**
 * The type Weapon.
 */
public class Weapon {

    private final InputSource inputSource;

    private final Validation validation;

    private final ConsoleOutput printer;

    /**
     * Instantiates a new Weapon.
     *
     * @param inputSource the input source
     * @param validation  the validation
     * @param printer     the printer
     */
    public Weapon(InputSource inputSource, Validation validation, ConsoleOutput printer) {
        this.inputSource = inputSource;
        this.validation = validation;
        this.printer = printer;
    }

    /**
     * Create weapon field weapon type.
     *
     * @return the weapon type
     */
    public WeaponType createWeaponField() {
        while (true) {
            try {
                printer.print(PrettyColor.yellow("Выберите желаемое оружие: "));
                for (int i = 0; i < WeaponType.values().length; i++) {
                    printer.print((i + 1) + ") " + WeaponType.values()[i]);
                }

                return getValidatedWeapon();
            } catch (InvalidInputException e) {
                printer.print(PrettyColor.red("Ошибка ввода, попробуйте снова"));
            }
        }
    }

    /**
     * Create weapon type field from script weapon type.
     *
     * @return the weapon type
     */
    public WeaponType createWeaponTypeFieldFromScript() {
        return getValidatedWeapon();
    }

    private WeaponType getValidatedWeapon() {
        final String userInput = inputSource.getTextFromUser().orElse("");

        if (WeaponType.contains(userInput)) {
            return WeaponType.getWeaponType(userInput);
        }

        if (validation.isInputForEnumIsInt(userInput)) {
            final int numOfWeapon = Integer.parseInt(userInput);

            if (validation.isValidInputForWeapon(numOfWeapon)) {
                return WeaponType.values()[numOfWeapon - 1];
            }
        }

        throw new InvalidInputException("Невалидные данные для поля Weapon");
    }
}
