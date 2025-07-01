package itmo.programming;

import itmo.programming.connection.ObjectTransport;
import itmo.programming.connection.authentication.GettingUserData;
import itmo.programming.exceptions.ExitCommandException;
import itmo.programming.input.ConsoleInput;
import itmo.programming.output.console.ConsoleControl;
import itmo.programming.output.console.ConsoleOutput;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;


/**
 * The type Client main.
 */
public class ClientMain implements AutoCloseable {

    private final DatagramChannel channel;
    private final ObjectTransport objectTransport;
    private final ConsoleControl consoleControl;

    /**
     * Instantiates a new Client main.
     *
     * @param host          the host
     * @param port          the port
     * @param consoleOutput the console output
     * @param consoleInput  the console input
     * @throws IOException the io exception
     */
    public ClientMain(
            String host,
            int port,
            ConsoleOutput consoleOutput, ConsoleInput consoleInput) throws IOException {
        this.channel = DatagramChannel.open();
        channel.configureBlocking(false);
        final InetSocketAddress address = new InetSocketAddress(host, port);

        this.objectTransport = new ObjectTransport(
                channel,
                address);
        this.consoleControl = new ConsoleControl(
                consoleInput,
                objectTransport,
                consoleOutput);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        final var consoleOutput = new ConsoleOutput();

        if (args.length < 2) {
            consoleOutput.printErr("Недостаточно аргументов для подключения к серверу");
            return;
        }
        try {
            final String host = args[0];
            final int port = Integer.parseInt(args[1]);
            final var consoleInput = new ConsoleInput(System.in);
            final var client = new ClientMain(host,
                    port,
                    consoleOutput,
                    new ConsoleInput(System.in));
            final var authentication = new GettingUserData(consoleInput, consoleOutput);
            client.start();
            client.close();
        } catch (UnknownHostException e) {
            consoleOutput.printErr("Неизвестный хост: " + args[0]);
        } catch (IOException e) {
            consoleOutput.printErr(("Произошла ошибка при подключении к серверу: " + e.getMessage()
                    + "\nПроверьте, что сервер запущен и доступен по указанному адресу и порту: "
                    + args[0] + " " + args[1]));
        } catch (NumberFormatException e) {
            consoleOutput.printErr(("Порт должен быть числом: " + args[1]));
        } catch (ExitCommandException e) {
            consoleOutput.printErr(PrettyColor.red("Клиент завершил работу"));
        }
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    private void start() throws ExitCommandException {
        while (true) {
            consoleControl.processNewCommand();
        }
    }
}
