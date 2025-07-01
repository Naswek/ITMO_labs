package itmo.programming.util;

import itmo.programming.exceptions.InvalidInputException;
import itmo.programming.object.HumanBeing;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Класс для управления коллекцией объектов {@link HumanBeing}.
 * Предоставляет методы для добавления, удаления, обновления, сортировки и получения информации
 * о коллекции, хранящейся в списке {@link #humanBeings}.
 */
public class ManageCollection {

    /**
     * Список, содержащий все объекты {@link HumanBeing} в коллекции.
     */
    private CopyOnWriteArrayList<HumanBeing> humanBeings;

    /**
     * Конструктор класса.
     * Инициализирует пустой список {@link #humanBeings} для хранения объектов.
     *
     */
    public ManageCollection(CopyOnWriteArrayList<HumanBeing> humanBeings) {
        this.humanBeings = humanBeings;
    }


    /**
     * Добавляет объект {@link HumanBeing} в коллекцию.
     *
     * @param human объект {@link HumanBeing} для добавления
     */
    public void add(HumanBeing human) {
        humanBeings.add(human);
    }

    /**
     * Удаляет объект из коллекции по указанному индексу.
     * В текущей реализации метод не завершён.
     *
     * @param index индекс объекта для удаления
     * @return the boolean
     * @throws IndexOutOfBoundsException если индекс выходит за пределы списка
     */
    public int isRemoveByIndex(int index) {
        if (humanBeings.size() <= index) {
            throw new InvalidInputException("Индекс выходит за пределы коллекции");
        } else {
            final int id = humanBeings.get(index).getId();
            humanBeings.remove(index);
            return id;
        }
    }

    /**
     * Очищает коллекцию, удаляя все объекты {@link HumanBeing}.
     *
     * @param id the index
     */
    public void clear(int id) {
        for (HumanBeing humanBeing : humanBeings) {
            if (humanBeing.getCreatorId() == id) {
                removeById(humanBeing.getId());
            }
        }
    }

    /**
     * Возвращает текущий размер коллекции.
     *
     * @return количество объектов {@link HumanBeing} в списке или 0 при ошибке
     */
    public int size() {
        return humanBeings.size();
    }

    /**
     * Gets humans for save.
     *
     * @return the humans for save
     */
    public ArrayList<HumanBeing> getHumansForSave() {
        return new ArrayList<>(humanBeings);
    }

    /**
     * Возвращает объект {@link HumanBeing} по идентификатору.
     *
     * @param id идентификатор объекта для поиска
     * @return объект {@link HumanBeing} с указанным ID или {@code null}, если не найден
     */
    public HumanBeing getHumanBeingById(int id) {
        for (HumanBeing h : humanBeings) {
            if (h.getId() == id) {
                return h;
            }
        }
        return null;
    }

    /**
     * Сортирует коллекцию по возрастанию идентификаторов объектов {@link HumanBeing}.
     * Использует {@link Comparator#comparingInt} с методом {@link HumanBeing#getId()}.
     */
    public void sort() {
        humanBeings.sort(Comparator.comparing(HumanBeing::getName));
    }

    /**
     * Remove by index.
     *
     * @param id the index
     */
    public void removeById(int id) {
        final HumanBeing human = getHumanBeingById(id);
        if (human != null) {
            humanBeings.remove(human);
        }
    }

    public int getId(int index) {
        if (humanBeings.size() <= index) {
            throw new InvalidInputException("Индекс выходит за пределы коллекции");
        } else {
            return humanBeings.get(index).getId();
        }
    }

    /**
     * Count less than soundtrack name int.
     *
     * @param soundtrackName the soundtrack name
     * @return the int
     */
    public int countLessThanSoundtrackName(String soundtrackName) {
        int count = 0;
        for (HumanBeing h : humanBeings) {
            if (h.getSoundtrackName().length() < soundtrackName.length()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Count greater than car int.
     *
     * @param parameter the parameter
     * @return the int
     */
    public int countGreaterThanCar(String parameter) {
        int count = 0;
        for (HumanBeing h : humanBeings) {
            final String carName = h.getCarName();
            if (carName.length() > parameter.length()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Filter contains name int.
     *
     * @param parameter the parameter
     * @return the int
     */
    public StringBuilder filterContainsName(String parameter) {
        int count = 0;
        final StringBuilder builder = new StringBuilder();
        for (HumanBeing humanBeing : humanBeings) {
            final String nameHuman = humanBeing.getName();
            final boolean contains = nameHuman.contains(parameter);

            if (contains) {
                builder.append(nameHuman).append("\n");
                count++;
            }
        }
        builder.append((count));
        return builder;
    }

    /**
     * Remove first human being.
     *
     */
    public void removeFirstHumanBeing() {
        for (HumanBeing humanBeing : humanBeings) {
            if (humanBeing != null) {
                final int id = humanBeing.getId();
                removeById(id);
            }
        }
        throw new InvalidInputException("Коллекция пуста, удаление невозможно");
    }

    /**
     * Update.
     *
     * @param humanBeing the human being
     * @param id         the index
     */
    public void update(HumanBeing humanBeing, int id) {
        removeById(id);
        humanBeings.add(humanBeing);
    }

    /**
     * Show all humans string.
     *
     * @return the string
     */
    public String showAllHumans() {
        final StringBuilder stringBuilder = new StringBuilder();
        if (!humanBeings.isEmpty()) {
            for (HumanBeing h : humanBeings) {
                if (h != null && h.getCar() != null) {
                    stringBuilder.append(h.toString());
                }
            }
            return stringBuilder.toString();
        } else {
            return "Коллекция пуста";
        }
    }


    /**
     * Is index valid boolean.
     *
     * @param id the index
     * @return the boolean
     */
    public boolean isIdValid(int id, int userId) {
        for (HumanBeing humanBeing : humanBeings) {
            if (humanBeing.getCreatorId() == userId
                    && humanBeing.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is index valid boolean.
     *
     * @param index the index
     * @return the boolean
     */
    public boolean isIndexValid(int index) {
        return humanBeings.size() > index;
    }

    /**
     * Show users objects string.
     *
     * @param id the index
     * @return the string
     */
    public String showUsersObjects(int id) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (HumanBeing humanBeing : humanBeings) {

            if (humanBeing.getCreatorId() == (id)) {
                stringBuilder.append(humanBeing.toString()).append("\n");
            }
        }
        if (stringBuilder.isEmpty()) {
            return "У вас нет объектов в коллекции";
        } else {
            return stringBuilder.toString();
        }
    }
}
