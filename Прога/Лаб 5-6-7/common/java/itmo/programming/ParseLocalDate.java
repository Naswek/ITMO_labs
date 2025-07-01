package itmo.programming;

import itmo.programming.exceptions.InvalidScriptInputException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Утилитарный класс для преобразования дат и времени между строками и объектами
 * {@link LocalDateTime}.
 * Предоставляет методы для форматирования {@link LocalDateTime} в строку и парсинга строки
 * в {@link LocalDateTime}.
 */
public class ParseLocalDate {

    /**
     * Начальный индекс для обрезки строки даты и времени в методе
     * {@link #parseLocalDateToString(LocalDateTime)}.
     * Определяет начало подстроки, содержащей "YYYY-MM-DDTHH:MM".
     */
    private static final int start = 0;

    /**
     * Конечный индекс для обрезки строки даты и времени в методе
     * {@link #parseLocalDateToString(LocalDateTime)}.
     * Определяет конец подстроки, обрезая до минут (16 символов: "YYYY-MM-DDTHH:MM").
     */
    private static final int end = 16;

    private static final int timeFormat = 2;
    private static final int dateFormat = 3;

    /**
     * Преобразует объект {@link LocalDateTime} в строковое представление.
     * Формат результата: "YYYY.MM.DD HH:MM", где дата и время обрезаются до минут.
     *
     * @param dateTime объект {@link LocalDateTime} для преобразования
     * @return строка в формате "YYYY.MM.DD HH:MM"
     */
    public static String parseLocalDateToString(LocalDateTime dateTime) {

        return dateTime.toString().substring(start, end)
                .replace("T", " ")
                .replace("-", ".");
    }

    /**
     * Парсит строку в объект {@link LocalDateTime}.
     * Ожидаемый формат строки: "YYYY.MM.DD HH:MM". Разделяет строку на дату и время,
     * преобразует их в {@link LocalDate} и {@link LocalTime}, затем объединяет в
     * {@link LocalDateTime}.
     *
     * @param dateTime строка с датой и временем в формате "YYYY.MM.DD HH:MM"
     * @return объект {@link LocalDateTime}, соответствующий переданной строке
     * @throws InvalidScriptInputException the invalid script input exception
     */
    public static LocalDateTime parseStringToLocalDate(String dateTime)
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
