package itmo.programming.output.parsers;

import itmo.programming.exceptions.InvalidScriptInputException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * The type Parse local date.
 */
public class ParseLocalDate {

    private static final int start = 0;

    private static final int end = 16;

    private static final int timeFormat = 2;
    private static final int dateFormat = 3;

    /**
     * Instantiates a new Parse local date.
     */
    public ParseLocalDate() {}


    /**
     * Parse local date to string string.
     *
     * @param dateTime the date time
     * @return the string
     */
    public static String parseLocalDateToString(LocalDateTime dateTime) {

        return dateTime.toString().substring(start, end)
                .replace("T", " ")
                .replace("-", ".");
    }

    /**
     * Parse string to local date local date time.
     *
     * @param dateTime the date time
     * @return the local date time
     * @throws InvalidScriptInputException the invalid script input exception
     */
    public LocalDateTime parseStringToLocalDate(String dateTime)
            throws InvalidScriptInputException {

        try {
            final String[] dateTimeArray = dateTime.split(" ");

            if (dateTimeArray.length == 2) {
                final String[] dateArray = dateTimeArray[0].split("\\.");
                final String[] timeArray = dateTimeArray[1].split(":");

                if (dateArray.length == dateFormat && timeArray.length == timeFormat) {
                    final LocalDate date = LocalDate.of(
                            Integer.parseInt(dateArray[0]), // Год
                            Integer.parseInt(dateArray[1]), // Месяц
                            Integer.parseInt(dateArray[2])); // День

                    final LocalTime time = LocalTime.of(
                            Integer.parseInt(timeArray[0]), // Часы
                            Integer.parseInt(timeArray[1])); // Минуты

                    return LocalDateTime.of(date, time);
                }
            }
        } catch (DateTimeException | NumberFormatException e) {
            return LocalDateTime.now();
        }
        return LocalDateTime.now();
    }
}
