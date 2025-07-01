package itmo.programming.input;

import itmo.programming.exceptions.InvalidInputException;
import java.util.Optional;

/**
 * The type Input source.
 */
public class InputSource {

    private final ConsoleInput reader;

    /**
     * Instantiates a new Input source.
     *
     * @param reader the reader
     */
    public InputSource(ConsoleInput reader) {
        this.reader = reader;
    }

    /**
     * Gets text from user.
     *
     * @return the text from user
     * @throws InvalidInputException the invalid input exception
     */
    public Optional<String> getTextFromUser() throws InvalidInputException {
        try {
            return Optional.of(reader.readLine().trim());
        } catch (InvalidInputException e) {
            System.err.println("Не удалось прочитать текст");
            return Optional.empty();
        }
    }

    /**
     * Gets number from user.
     *
     * @return the number from user
     * @throws InvalidInputException the invalid input exception
     */
    public Optional<Double> getNumberFromUser() throws InvalidInputException {
        return getTextFromUser().flatMap(text -> {
            if (text.isEmpty()) {
                return Optional.empty();
            }
            try {
                return Optional.of(Double.parseDouble(text.replace(',', '.')));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });
    }

    /**
     * Gets int from user.
     *
     * @return the int from user
     * @throws InvalidInputException the invalid input exception
     */
    public Optional<Integer> getIntFromUser() throws InvalidInputException {
        return getNumberFromUser().map(Double::intValue);
    }

    /**
     * Gets float from user.
     *
     * @return the float from user
     * @throws InvalidInputException the invalid input exception
     */
    public Optional<Float> getFloatFromUser() throws InvalidInputException {
        return getNumberFromUser().map(Double::floatValue);
    }

    /**
     * Gets long from user.
     *
     * @return the long from user
     * @throws InvalidInputException the invalid input exception
     */
    public Optional<Long> getLongFromUser() throws InvalidInputException {
        return getNumberFromUser().map(Double::longValue);
    }


}
