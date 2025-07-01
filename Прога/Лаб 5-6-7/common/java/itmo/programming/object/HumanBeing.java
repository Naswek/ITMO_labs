package itmo.programming.object;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Класс, представляющий объект "Человек" (HumanBeing) с различными характеристиками.
 * Содержит поля, описывающие идентификатор, имя, координаты, дату создания и другие
 * свойства, включая транспортное средство и настроение. Используется для хранения
 */
public class HumanBeing implements Serializable {

    /**
     * Уникальный идентификатор объекта.
     * Значение поля должно быть больше 0, генерируется автоматически.
     */
    private int id;

    /**
     * Имя человека.
     * Поле не может быть null, строка не может быть пустой.
     */
    private String name;

    /**
     * Координаты объекта.
     */
    private Coordinates coordinates;

    /**
     * Дата и время создания объекта.
     * Устанавливается автоматически при создании объекта.
     */
    private LocalDateTime creationDate;

    /**
     * Флаг, указывающий, является ли человек настоящим героем.
     */
    private boolean realHero;

    /**
     * Флаг, указывающий, есть ли у человека зубочистка.
     */
    private boolean hasToothpick;

    /**
     * Скорость удара.
     * Значение поля должно быть больше -442.
     */
    private float impactSpeed;

    /**
     * Название саундтрека.
     * Поле не может быть null.
     */
    private String soundtrackName;

    /**
     * Тип оружия.
     * Поле не может быть null, содержит значение из перечисления {@link WeaponType}.
     */
    private WeaponType weaponType;

    /**
     * Настроение человека.
     * Поле не может быть null, содержит значение из перечисления {@link Mood}.
     */
    private Mood mood;

    /**
     * Транспортное средство человека.
     * Поле не может быть null, содержит объект класса {@link Car}.
     */
    private Car car;

    private int creatorId;

    /**
     * Создает новый объект HumanBeing с указанными характеристиками.
     * Используется аннотацией {@link JsonCreator} для десериализации JSON.
     *
     * @param id             уникальный идентификатор (должен быть > 0)
     * @param name           имя человека (не null, не пустая строка)
     * @param coordinates    координаты объекта ({@link Coordinates}, не null)
     * @param creationDate   дата и время создания ({@link LocalDateTime})
     * @param realHero       флаг настоящего героя
     * @param hasToothpick   флаг наличия зубочистки
     * @param impactSpeed    скорость удара (должна быть > -442)
     * @param soundtrackName название саундтрека (не null)
     * @param weaponType     тип оружия ({@link WeaponType}, не null)
     * @param mood           настроение ({@link Mood}, не null)
     * @param car            транспортное средство ({@link Car}, не null)
     * @param creatorId      the creator index
     */
    @JsonCreator
    public HumanBeing(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("coordinates") Coordinates coordinates,
            @JsonProperty("creationDate") LocalDateTime creationDate,
            @JsonProperty("realHero") boolean realHero,
            @JsonProperty("hasToothpick") boolean hasToothpick,
            @JsonProperty("impactSpeed") float impactSpeed,
            @JsonProperty("soundtrack") String soundtrackName,
            @JsonProperty("weaponType") WeaponType weaponType,
            @JsonProperty("mood") Mood mood,
            @JsonProperty("car") Car car,
            int creatorId
    ) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.realHero = realHero;
        this.hasToothpick = hasToothpick;
        this.impactSpeed = impactSpeed;
        this.soundtrackName = soundtrackName;
        this.car = car;
        this.mood = mood;
        this.weaponType = weaponType;
        this.creatorId = creatorId;
    }

    /**
     * Возвращает уникальный идентификатор объекта.
     *
     * @return идентификатор (int, > 0)
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает имя человека.
     *
     * @return имя (String, не null, не пустая строка)
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает координаты объекта.
     *
     * @return объект {@link Coordinates}, представляющий координаты (не null)
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Возвращает дату и время создания объекта.
     *
     * @return объект {@link LocalDateTime}, представляющий дату создания
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Проверяет, является ли человек настоящим героем.
     *
     * @return true, если человек настоящий герой, false в противном случае
     */
    public boolean isRealHero() {
        return realHero;
    }

    /**
     * Проверяет, есть ли у человека зубочистка.
     *
     * @return true, если у человека есть зубочистка, false в противном случае
     */
    public boolean isHasToothpick() {
        return hasToothpick;
    }

    /**
     * Возвращает скорость удара.
     *
     * @return скорость удара (float, > -442)
     */
    public float getImpactSpeed() {
        return impactSpeed;
    }

    /**
     * Возвращает название саундтрека.
     *
     * @return название саундтрека (String, не null)
     */
    public String getSoundtrackName() {
        return soundtrackName;
    }

    /**
     * Возвращает настроение человека.
     *
     * @return объект {@link Mood}, представляющий настроение (не null)
     */
    public Mood getMood() {
        return mood;
    }

    /**
     * Возвращает транспортное средство человека.
     *
     * @return объект {@link Car}, представляющий транспортное средство (не null)
     */
    public Car getCar() {
        return car;
    }

    /**
     * Возвращает тип оружия человека.
     *
     * @return объект {@link WeaponType}, представляющий тип оружия (не null)
     */
    public WeaponType getWeaponType() {
        return weaponType;
    }

    /**
     * Устанавливает новый идентификатор объекта.
     *
     * @param newId новый идентификатор (должен быть > 0)
     */
    public void setId(int newId) {
        id = newId;
    }

    /**
     * Устанавливает новое имя человека.
     *
     * @param newName новое имя (не null, не пустая строка)
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Устанавливает новые координаты объекта.
     *
     * @param newCoordinates новые координаты ({@link Coordinates}, не null)
     */
    public void setCoordinates(Coordinates newCoordinates) {
        coordinates = newCoordinates;
    }

    /**
     * Устанавливает новую дату и время создания объекта.
     *
     * @param newCreationDate новая дата создания ({@link LocalDateTime})
     */
    public void setCreationDate(LocalDateTime newCreationDate) {
        creationDate = newCreationDate;
    }

    /**
     * Устанавливает флаг, является ли человек настоящим героем.
     *
     * @param newRealHero новый флаг (true/false)
     */
    public void setIsRealHero(boolean newRealHero) {
        realHero = newRealHero;
    }

    /**
     * Устанавливает флаг наличия зубочистки у человека.
     *
     * @param newHasToothpick новый флаг (true/false)
     */
    public void setIsHasToothpick(boolean newHasToothpick) {
        hasToothpick = newHasToothpick;
    }

    /**
     * Устанавливает новую скорость удара.
     *
     * @param newImpactSpeed новая скорость удара (должна быть > -442)
     */
    public void setImpactSpeed(float newImpactSpeed) {
        impactSpeed = newImpactSpeed;
    }

    /**
     * Устанавливает новое название саундтрека.
     *
     * @param newSoundtrackName новое название саундтрека (не null)
     */
    public void setSoundtrackName(String newSoundtrackName) {
        soundtrackName = newSoundtrackName;
    }

    /**
     * Устанавливает новое настроение человека.
     *
     * @param newMood новое настроение ({@link Mood}, не null)
     */
    public void setMood(Mood newMood) {
        mood = newMood;
    }

    /**
     * Устанавливает новое транспортное средство человека.
     *
     * @param newCar новое транспортное средство ({@link Car}, не null)
     */
    public void setCar(Car newCar) {
        car = newCar;
    }

    /**
     * Устанавливает новый тип оружия человека.
     *
     * @param newType новый тип оружия ({@link WeaponType}, не null)
     */
    public void setWeaponType(WeaponType newType) {
        weaponType = newType;
    }

    /**
     * Возвращает имя транспортного средства человека.
     * Используется аннотацией {@link JsonIgnore}, чтобы исключить из сериализации JSON.
     *
     * @return имя транспортного средства ({@link Car#getName()})
     */
    @JsonIgnore
    public String getCarName() {
        return car.getName();
    }


    /**
     * Gets creator index.
     *
     * @return the creator index
     */
    public int getCreatorId() {
        return creatorId;
    }

    /**
     * Sets creator index.
     *
     * @param creatorId the creator index
     */
    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String toString() {
        return "\n-----------------"
                + "\n  id = " + id
                + ",\n  name = " + name
                + ",\n  coordinates = " + coordinates.getFirstElement()
                + ", " + coordinates.getSecondElement()
                + ",\n  creationDate = " + getCreationDate()
                + ",\n  realHero = " + realHero
                + ",\n  hasToothpick = " + hasToothpick
                + ",\n  impactSpeed = " + impactSpeed
                + ",\n  soundtrackName = " + soundtrackName
                + ",\n  weaponType = " + weaponType
                + ",\n  mood = " + mood
                + ",\n  car name = " + car.getName()
                + ",\n  car cool = " + car.isCool()
                + ",\n  creator_id = " + creatorId;
    }
}
