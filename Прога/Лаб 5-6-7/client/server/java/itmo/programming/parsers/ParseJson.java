package itmo.programming.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import itmo.programming.exceptions.CollectionLoadException;
import itmo.programming.object.HumanBeing;
import itmo.programming.util.ManageCollection;
import itmo.programming.util.ReadHuman;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Утилитарный класс для записи объектов из {@link ManageCollection} в JSON-формат.
 * Предоставляет статический метод для преобразования коллекции объектов в список строк JSON.
 */
public class ParseJson {

    /** Статический экземпляр {@link ObjectMapper} для записи объектов в JSON. */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Преобразует коллекцию объектов из {@link ManageCollection} в список строк в формате JSON.
     * Использует {@link ObjectMapper} с зарегистрированным модулем {@link JavaTimeModule}
     * для поддержки
     * типов Java Time API (например, {@link LocalDateTime}). Записывает список объектов,
     * полученный через, в одну JSON-строку с
     * форматированием
     * Pretty Print и возвращает её в виде списка из одного элемента.
     *
     * @param manageCollection объект {@link ManageCollection}, содержащий коллекцию для записи
     * @return список {@link ArrayList}, содержащий одну строку JSON с записанной коллекцией
     * @throws IOException если возникает ошибка при записи объектов в JSON
     */
    public ArrayList<String> parseObjectsToJson(ManageCollection manageCollection)
            throws IOException {
        return getStrings(manageCollection, mapper);
    }

    /**
     * Gets strings.
     *
     * @param manageCollection the manage collection
     * @param mapper           the mapper
     * @return the strings
     * @throws JsonProcessingException the json processing exception
     */
    public static ArrayList<String> getStrings(ManageCollection manageCollection,
                                               ObjectMapper mapper) throws JsonProcessingException {
        final ArrayList<String> lines = new ArrayList<>();
        mapper.registerModule(new JavaTimeModule());
        mapper.configOverride(LocalDateTime.class);
        final String jsonLine = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(manageCollection.getHumansForSave());
        lines.add(jsonLine);
        return lines;
    }


    /**
     * Parse json to objects array list.
     *
     * @param fileName    the file name
     * @param humanReader the human reader
     * @return the array list
     */
    public ArrayList<HumanBeing> parseJsonToObjects(String fileName, ReadHuman humanReader) {
        try {
            return getHumanBeings(fileName, humanReader, mapper);
        } catch (IOException e) {
            throw new CollectionLoadException("Произошла ошибка при загрузке массива");
        }
    }

    /**
     * Gets human beings.
     *
     * @param fileName    the file name
     * @param humanReader the human reader
     * @param mapper      the mapper
     * @return the human beings
     * @throws JsonProcessingException the json processing exception
     */
    public static ArrayList<HumanBeing> getHumanBeings(
            String fileName,
            ReadHuman humanReader,
            ObjectMapper mapper) throws JsonProcessingException {
        mapper.registerModule(new JavaTimeModule());
        final String jsonLine = humanReader.jsonRead(fileName);
        final ArrayList<HumanBeing> humans = mapper.readValue(
                jsonLine,
                mapper.getTypeFactory().constructCollectionType(
                        ArrayList.class,
                        HumanBeing.class));
        System.out.println("Коллекция успешно загружена!)");
        return humans;
    }
}
