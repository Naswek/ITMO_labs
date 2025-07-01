package itmo.programming.creator.fields;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.exceptions.InvalidScriptInputException;
import itmo.programming.input.InputSource;
import itmo.programming.object.Car;
import itmo.programming.output.console.ConsoleOutput;

/**
 * The type Car create.
 */
public class CarCreate {

    private final String[] booleanValues = {"True", "False"};

    private final InputSource inputSource;

    private final Validation validation;

    private final ConsoleOutput printer;

    /**
     * Instantiates a new Car create.
     *
     * @param inputSource the input source
     * @param validation  the validation
     * @param printer     the printer
     */
    public CarCreate(InputSource inputSource, Validation validation, ConsoleOutput printer) {
        this.inputSource = inputSource;
        this.validation = validation;
        this.printer = printer;
    }

    /**
     * Create car field car.
     *
     * @return the car
     */
    public Car createCarField() {
        return createCar(requestCarName(), requestCoolness());
    }

    /**
     * Create car from script car.
     *
     * @return the car
     */
    public Car createCarFromScript() {
        try {
            final String nameCar = inputSource.getTextFromUser().orElse(" ");

            final int coolness = inputSource.getIntFromUser().orElse(-1);

            if (validation.isValidInputForBoolean(coolness)) {
                final boolean isCool = parseCoolness(coolness);
                return createCar(nameCar, isCool);
            }

            throw new InvalidInputException("Невалидные данные для поля Car");
        } catch (InvalidInputException e) {
            throw new InvalidScriptInputException("Невалидные данные для поля Car в скрипте");
        }
    }

    /**
     * Parse coolness boolean.
     *
     * @param userInput the user input
     * @return the boolean
     */
    public boolean parseCoolness(int userInput) {
        return Boolean.parseBoolean(booleanValues[userInput - 1]);
    }

    /**
     * Create car car.
     *
     * @param name the name
     * @param cool the cool
     * @return the car
     */
    public Car createCar(String name, boolean cool) {
        return new Car(name, cool);
    }

    private String requestCarName() {
        printer.print(PrettyColor.yellow("Выберите имя машины: "));
        return inputSource.getTextFromUser().orElse(" ");
    }

    private boolean requestCoolness() {
        while (true) {
            printer.print(PrettyColor.yellow("Это крутая машина?: "));
            for (int i = 0; i < booleanValues.length; i++) {
                printer.print((i + 1) + ") " + booleanValues[i]);
            }

            final int userInput = inputSource.getIntFromUser().orElse(0);

            if (validation.isValidInputForBoolean(userInput)) {
                return parseCoolness(userInput);
            } else {
                printer.print(PrettyColor.red("Напишите цифру в пределах допустимых"));
            }
        }
    }
}


