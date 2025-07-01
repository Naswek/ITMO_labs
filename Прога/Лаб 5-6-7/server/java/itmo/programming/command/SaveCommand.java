package itmo.programming.command;

import itmo.programming.exceptions.FailedExecution;
import itmo.programming.parsers.ParseJson;
import itmo.programming.util.ManageCollection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс команды для сохранения коллекции в файл.
 * Реализует интерфейс {@link Command} и предоставляет функциональность
 * для сериализации коллекции, управляемой объектом {@link ManageCollection},
 * в JSON-формат с последующей записью в файл по заданному пути.
 */
public class SaveCommand {

    /**
     * Объект управления коллекцией, которая будет сохранена в файл.
     */
    private final ManageCollection manageCollection;

    /**
     * Путь к файлу для сохранения.
     */
    private final String filePath;

    /**
     * Парсер для преобразования в JSON.
     */
    private final ParseJson jsonParser;


    /**
     * Конструктор класса команды.
     *
     * @param manageCollection объект {@link ManageCollection} с коллекцией для сохранения
     * @param filePath         путь к файлу, в который будет сохранена коллекция
     * @param jsonParser       объект {@link ParseJson} для преобразования коллекции в JSON
     */
    public SaveCommand(
            ManageCollection manageCollection,
            String filePath,
            ParseJson jsonParser) {
        this.manageCollection = manageCollection;
        this.filePath = filePath;
        this.jsonParser = jsonParser;
    }

    /**
     * Save.
     *
     * @throws FailedExecution the failed execution
     */
    public void save() {
        if (filePath != null) {

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                final ArrayList<String> jsonLines = jsonParser.parseObjectsToJson(manageCollection);
                for (String jsonLine : jsonLines) {
                    writer.write(jsonLine);
                }
                System.out.println("Коллекция подгружается...\n" + "Коллекция сохранена!");
            } catch (IOException e) {
                System.err.println("Невозможно сохранить коллекцию");
            }
        }
    }
}
