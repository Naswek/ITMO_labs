package itmo.programming.object;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Содержит имя автомобиля и флаг, указывающий, является ли автомобиль "крутым".
 * Используется для описания транспортного средства человека в коллекции.
 */
public class Car implements Serializable {

    /**
     * Имя автомобиля.
     * Поле может быть null.
     */
    private String name;

    /**
     * Флаг, указывающий, является ли автомобиль "крутым".
     */
    private boolean cool;

    /**
     * Создает новый объект Car с указанными характеристиками.
     * Используется аннотацией {@link JsonCreator} для десериализации JSON.
     *
     * @param name имя автомобиля (может быть null)
     * @param cool флаг, указывающий, является ли автомобиль "крутым"
     */
    @JsonCreator
    public Car(
            @JsonProperty("nameCar") String name,
            @JsonProperty("isCool") boolean cool
    ) {
        this.name = name;
        this.cool = cool;
    }

    /**
     * Возвращает имя автомобиля.
     *
     * @return имя автомобиля (String, может быть null)
     */
    public String getName() {
        return name;
    }

    /**
     * Проверяет, является ли автомобиль "крутым".
     *
     * @return true, если автомобиль "крутой", false в противном случае
     */
    public boolean isCool() {
        return cool;
    }

    /**
     * Устанавливает новое имя автомобиля.
     *
     * @param newName новое имя автомобиля (может быть null)
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Устанавливает флаг, указывающий, является ли автомобиль "крутым".
     *
     * @param newCool новый флаг (true/false)
     */
    public void setCool(boolean newCool) {
        cool = newCool;
    }
}
