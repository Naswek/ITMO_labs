package itmo.programming;

import itmo.programming.connection.ObjectConverter;
import itmo.programming.connection.ObjectTransport;
import itmo.programming.exceptions.CollectionLoadException;
import itmo.programming.storage.ConnectionDb;
import itmo.programming.storage.LoadDb;
import itmo.programming.util.Initialization;
import itmo.programming.util.ReadHuman;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * The type Server.
 */
public class Server {

    private final DatagramSocket socket;
    private final int bufferSize = 65507;
    private final ObjectTransport objectTransport;
    private final ExecutorService requestExecutor;
    private final ExecutorService responseExecutor;
    private final Semaphore semaphore;
    private final int tasksLimit = 100;
    private final int threadsLimit = 4;



    /**
     * Instantiates a new Server.
     *
     * @param fileName       the file name
     * @param port           the port
     * @param initialization the initialization
     * @throws SocketException the socket exception
     */
    public Server(String fileName, int port, Initialization initialization) throws SocketException {
        this.socket = new DatagramSocket(port);
        this.objectTransport = new ObjectTransport(
                socket,
                new ObjectConverter(),
                initialization);
        requestExecutor = Executors.newFixedThreadPool(threadsLimit);
        responseExecutor = Executors.newCachedThreadPool();
        this.semaphore = new Semaphore(tasksLimit);
    }

    /**
     * Start.
     */
    public void start() {
        while (true) {
            try {
                semaphore.acquire(); // Ограничиваем количество задач
                requestExecutor.submit(() -> {
                    try {
                        objectTransport.receiveAndSend(requestExecutor, responseExecutor);
                    } finally {
                        semaphore.release(); // Освобождаем место после завершения задачи
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Ошибка при обработке задачи: " + e.getMessage());
            }
        }
    }


    private void run() {
        final Thread thread = new Thread(this::start);
        thread.start();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Недостаточно параметров для запуска сервера");
            return;
        }

        final String fileName = args[0];
        final int port = Integer.parseInt(args[1]);

        try {
            final var humanReader = new ReadHuman(System.in);
            final var connectionDb = new ConnectionDb();
            final var initialization = new Initialization(humanReader, connectionDb,
                    new LoadDb(connectionDb));
            final var server = new Server(fileName, port, initialization);
            initialization.startInitialization(fileName);
            server.run();

            Runtime.getRuntime().addShutdownHook(new Thread(initialization::saveCollection));

        } catch (SocketException e) {
            System.err.println("Произошла ошибка подключения: " + " " + port);
        } catch (NumberFormatException e) {
            System.err.println("Порт должен быть числом: " + args[1]);
        } catch (CollectionLoadException e) {
            System.err.println("Произошла ошибка при загрузке коллекции: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
