package itmo.programming.util;

import itmo.programming.command.AddCommand;
import itmo.programming.command.ClearCommand;
import itmo.programming.command.Command;
import itmo.programming.command.CountGreaterThanCarCommand;
import itmo.programming.command.CountLessThanSoundtrackNameCommand;
import itmo.programming.command.FilterContainsNameCommand;
import itmo.programming.command.HelpCommand;
import itmo.programming.command.InfoCommand;
import itmo.programming.command.ReadScriptCommand;
import itmo.programming.command.RemoveByIdCommand;
import itmo.programming.command.RemoveByIndexCommand;
import itmo.programming.command.RemoveFirstCommand;
import itmo.programming.command.SaveCommand;
import itmo.programming.command.ShowCommand;
import itmo.programming.command.SortCommand;
import itmo.programming.command.UpdateCommand;
import itmo.programming.command.UserObjectsCommand;
import itmo.programming.command.validation.CheckId;
import itmo.programming.command.validation.CheckIndex;
import itmo.programming.command.validation.CheckParameter;
import itmo.programming.command.validation.CheckUser;
import itmo.programming.command.validation.UserCredentialsValidation;
import itmo.programming.exceptions.CollectionLoadException;
import itmo.programming.parsers.ParseJson;
import itmo.programming.requests.AddRequest;
import itmo.programming.requests.AuthenticationRequest;
import itmo.programming.requests.ClearRequest;
import itmo.programming.requests.CountGreaterThanCarRequest;
import itmo.programming.requests.CountLessThanSoundtrackNameRequest;
import itmo.programming.requests.FilterContainsNameRequest;
import itmo.programming.requests.HelpRequest;
import itmo.programming.requests.InfoRequest;
import itmo.programming.requests.RemoveByIdRequest;
import itmo.programming.requests.RemoveByIndexRequest;
import itmo.programming.requests.RemoveFirstRequest;
import itmo.programming.requests.ShowRequest;
import itmo.programming.requests.SortRequest;
import itmo.programming.requests.UpdateRequest;
import itmo.programming.requests.UserObjectsRequest;
import itmo.programming.requests.ValidaitionIndexRequest;
import itmo.programming.requests.ValidationIdRequest;
import itmo.programming.storage.ConnectionDb;
import itmo.programming.storage.LoadDb;
import itmo.programming.storage.ManageDatabase;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Класс для инициализации программы.
 * Отвечает за настройку начальных данных, создание коллекции команд и запуск программы.
 */
public class Initialization {

    /**
     * Дата и время инициализации программы.
     */
    private HashMap<Class<?>, Command> commands;
    private HashMap<Class<?>, CheckParameter> checks;
    private HashMap<Class<?>, CheckUser> authenticationsChecks;
    private final LocalDateTime initializationDate;
    private final ManageCollection collectionManager;
    private final ManageDatabase manageDatabase;
    private final ParseJson jsonParser;
    private final String filePath = System.getenv("FILE_PATH");
    private final Validation validation;
    private final ReadHuman humanReader;
    private final SaveCommand saveCommand;

    /**
     * Конструктор класса.
     * Создаёт пустой экземпляр для последующей инициализации через метод
     *
     * @param humanReader  the human reader
     * @param connectionDb the connection db
     * @param loadDb       the load db
     */
    public Initialization(ReadHuman humanReader, ConnectionDb connectionDb, LoadDb loadDb) {
        validation = new Validation();
        manageDatabase = new ManageDatabase(connectionDb);
        collectionManager = new ManageCollection(loadDb.loadAllHumans());
        jsonParser = new ParseJson();
        this.initializationDate = LocalDateTime.now();
        this.humanReader = humanReader;
        this.saveCommand = new SaveCommand(collectionManager, filePath, jsonParser);
    }

    /**
     * Выполняет инициализацию программы.
     * Создаёт коллекцию, заполняет карту команд, загружает данные из файла, если он существует,
     *
     * @param fileName the file name
     * @throws CollectionLoadException если возникает ошибка ввода-вывода при работе с файлом
     */
    public void startInitialization(String fileName) throws CollectionLoadException {
        initializeCommandsAndChecks();
        startConsole();
    }

    private void initializeCommandsAndChecks() {
        commands = new HashMap<>() {{
                put(AddRequest.class, new AddCommand(
                        collectionManager, validation, manageDatabase));
                put(HelpRequest.class, new HelpCommand(Initialization.this));
                put(InfoRequest.class, new InfoCommand(collectionManager, Initialization.this));
                put(ShowRequest.class, new ShowCommand(collectionManager));
                put(SortRequest.class, new SortCommand(collectionManager));
                put(ClearRequest.class, new ClearCommand(collectionManager, manageDatabase));
                put(UpdateRequest.class, new UpdateCommand(
                        collectionManager, validation, manageDatabase));
                put(RemoveByIndexRequest.class, new RemoveByIndexCommand(
                        collectionManager, validation, manageDatabase));
                put(RemoveByIdRequest.class, new RemoveByIdCommand(
                        collectionManager, validation, manageDatabase));
                put(RemoveFirstRequest.class, new RemoveFirstCommand(
                        collectionManager, manageDatabase));
                put(FilterContainsNameRequest.class, new FilterContainsNameCommand(
                        collectionManager));
                put(CountGreaterThanCarRequest.class, new CountGreaterThanCarCommand(
                        collectionManager));
                put(CountLessThanSoundtrackNameRequest.class,
                    new CountLessThanSoundtrackNameCommand(collectionManager));
                put(null, new ReadScriptCommand());
                put(UserObjectsRequest.class, new UserObjectsCommand(
                        collectionManager, manageDatabase));
            }};

        checks = new HashMap<>() {{
                put(ValidaitionIndexRequest.class, new CheckIndex(collectionManager));
                put(ValidationIdRequest.class, new CheckId(collectionManager, manageDatabase));
            }};

        authenticationsChecks = new HashMap<>() {{
                put(AuthenticationRequest.class, new UserCredentialsValidation(manageDatabase));
            }};
    }

    /**
     * Возвращает дату и время инициализации программы.
     *
     * @return объект {@link LocalDateTime} с датой и временем инициализации
     */
    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    /**
     * Возвращает карту доступных команд.
     *
     * @return объект {@link HashMap} с командами, значение — объект {@link Command}
     */
    public HashMap<Class<?>, Command> getCommands() {
        return commands;
    }

    /**
     * Gets checks.
     *
     * @return the checks
     */
    public HashMap<Class<?>, CheckParameter> getChecks() {
        return checks;
    }

    /**
     * Gets authentications checks.
     *
     * @return the authentications checks
     */
    public HashMap<Class<?>, CheckUser> getAuthenticationsChecks() {
        return authenticationsChecks;
    }

    /**
     * Save collection.
     */
    public void saveCollection() {
        saveCommand.save();
    }

    private void startConsole() {
        final Thread daemonThread = new Thread(this::readConsole);
        daemonThread.setDaemon(true);
        daemonThread.start();
    }

    private void readConsole() {
        while (humanReader.hasNextLine()) {
            if (humanReader.readLine().equals("save")) {
                saveCollection();
            }
        }
    }
}
