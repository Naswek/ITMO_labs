package itmo.programming.output.console;

import itmo.programming.PrettyColor;
import itmo.programming.command.CommandsMap;
import itmo.programming.connection.ObjectTransport;
import itmo.programming.connection.authentication.GettingUserData;
import itmo.programming.creator.PromptObjects;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.exceptions.NullRequestException;
import itmo.programming.exceptions.RequestCreationException;
import itmo.programming.exepctions.LogoutException;
import itmo.programming.input.ConsoleInput;
import itmo.programming.message.OutputMessage;
import itmo.programming.util.ClientControlCommand;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * The type Console control.
 */
public class ConsoleControl {

    private String login = null;
    private final ObjectTransport objectTransport;
    private final ConsoleInput consoleInput;
    private final ConsoleOutput consoleOutput;
    private boolean isAuthenticated = false;
    private ClientControlCommand controlCommandR;

    /**
     * Instantiates a new Console control.
     *
     * @param consoleInput    the console input
     * @param objectTransport the object transport
     * @param consoleOutput   the console output
     */
    public ConsoleControl(ConsoleInput consoleInput,
                          ObjectTransport objectTransport,
                          ConsoleOutput consoleOutput) {
        this.consoleInput = consoleInput;
        this.objectTransport = objectTransport;
        this.consoleOutput = consoleOutput;
        this.login = null;
        this.isAuthenticated = false;
        this.controlCommandR = null;
    }

    /**
     * Process new command.
     */
    public void processNewCommand() {
        try {
            setCommandControl();
            if (isAuthenticated) {
                if (!consoleInput.isSimulatedStream()) {
                    consoleOutput.print(PrettyColor.green(
                            "Введите команду (help для вывода списка команд)"));
                    consoleOutput.print(PrettyColor.green(
                            "Если вы хотите выйти с аккаунта, пропишите 'logout'"));
                }
                consoleOutput.print(objectTransport.sendPackage(consoleInput.readLine()).message());
            } else {
                consoleOutput.print("Напишите команду 'login', что начать аутентификацию");
                final var message = objectTransport.sendPackageWithUserData(controlCommandR.convertInput(consoleInput.readLine()));
                isAuthenticated = message.flag();
                login = message.login();
                consoleOutput.print(message.message());
            }
        } catch (UnknownHostException e) {
            consoleOutput.printErr("Произошла ошибка при попытке подключения к серверу: ");
        } catch (SocketException e) {
            consoleOutput.printErr("Произошла ошибка при попытке отправки запроса");
        } catch (IOException e) {
            consoleOutput.printErr("Произошла ошибка в клиенте: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            consoleOutput.printErr("Произошла ошибка при получении данных: " + e.getMessage());
        } catch (ClassCastException e) {
            consoleOutput.printErr("Произошла ошибка приведения типов: " + e.getMessage());
        } catch (RequestCreationException | FailedExecution e) {
            consoleOutput.printErr("Произошла ошибка создания запроса: " + e.getMessage());
        } catch (NullRequestException e) {
            //ignored
        } catch (LogoutException e) {
            isAuthenticated = false;
            login = null;
        }
    }

    private void setCommandControl() {
        final var controlCommand = new ClientControlCommand(
                new CommandsMap(
                        new PromptObjects(
                                consoleOutput,
                                consoleInput
                        ),
                        consoleInput,
                        ConsoleControl.this,
                        consoleOutput
                ), login
        );
        objectTransport.setCommandControl(controlCommand);
        this.controlCommandR = controlCommand;
    }
}
