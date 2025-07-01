package itmo.programming.object;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Класс, представляющий координаты объекта в двумерном пространстве.
 * Содержит две координаты: первую (долгосрочное значение) и вторую (число с плавающей точкой),
 * которые используются для определения положения объекта, например, в коллекции
 */
public class Coordinates implements Serializable {

    /**
     * Первая координата (X).
     * Значение поля должно быть больше -499.
     */
    private final long firstElement;

    /**
     * Вторая координата (Y).
     * Максимальное значение поля: 672, поле не может быть null.
     */
    private final Float secondElement;

    /**
     * Создает новый объект Coordinates с указанными координатами.
     * Используется аннотацией {@link JsonCreator} для перезаписи JSON.
     *
     * @param firstElement  первая координата (должна быть > -499)
     * @param secondElement вторая координата (не null, максимум 672)
     */
    @JsonCreator
    public Coordinates(
            @JsonProperty("valueX") long firstElement,
            @JsonProperty("valueY") Float secondElement
    ) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    /**
     * Возвращает первую координату (X).
     *
     * @return первая координата (long, > -499)
     */
    public long getFirstElement() {
        return firstElement;
    }

    /**
     * Возвращает вторую координату (Y).
     *
     * @return вторая координата (float, не null, максимум 672)
     */
    public Float getSecondElement() {
        return secondElement;
    }
}
