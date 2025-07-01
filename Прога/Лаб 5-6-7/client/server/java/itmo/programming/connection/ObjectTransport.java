package itmo.programming.connection;

import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import itmo.programming.util.Initialization;
import itmo.programming.util.ManageCommand;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Object transport.
 */
public class ObjectTransport {

    private final ManageCommand commandManager;
    private final DatagramSocket socket;
    private final ObjectConverter converter;
    private final int bufferSize = 65507;
    private final Logger logger = LogManager.getLogger(ObjectTransport.class);

    /**
     * Instantiates a new Object transport.
     *
     * @param socket         the socket
     * @param converter      the converter
     * @param initialization the initialization
     */
    public ObjectTransport(DatagramSocket socket,
                           ObjectConverter converter,
                           Initialization initialization) {
        this.socket = socket;
        this.converter = converter;
        this.commandManager = new ManageCommand(initialization);
    }

    private AddressResponse receivePackage() throws IOException, ClassNotFoundException {

        final ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
        final DatagramPacket requestPacket = new DatagramPacket(
                byteBuffer.array(),
                byteBuffer.array().length);
        byteBuffer.clear();
        socket.receive(requestPacket);
        logger.info("получен пакет");

        final AddressRequest addressRequest = converter.deserializeRequest(requestPacket);
        final Request request = addressRequest.request();
        if (request == null) {
            return new AddressResponse(addressRequest.socketAddress(),
                    new MessageResponse("отправлен пустой реквест"));
        }
        return new AddressResponse(addressRequest.socketAddress(),
                commandManager.executeCommand(request));
    }

    /**
     * Send package.
     *
     * @param addressResponse the address response
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public void sendPackage(AddressResponse addressResponse)
            throws IOException, ClassNotFoundException {
        logger.info("отправляется пакет");
        socket.send(converter.serializeResponse(addressResponse));
    }

    /**
     * Receive and send.
     *
     * @param requestExecutor  the request executor
     * @param responseExecutor the response executor
     */
    public void receiveAndSend(ExecutorService requestExecutor, ExecutorService responseExecutor) {
        try {
            final AddressResponse addressResponse = receivePackage();
            tryToSend(responseExecutor, addressResponse);
        } catch (UnknownHostException | SocketException e) {
            logger.error("Произошла ошибка при попытке подключения к клиенту: "
                    + e.getCause());
        } catch (IOException e) {
            logger.error("Произошла ошибка на сервере: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.error("Произошла ошибка при получении данных: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException e) {
            logger.error("Произошла ошибка приведения типов: " + e.getMessage());
        }
    }

    /**
     * Try to send.
     *
     * @param responseExecutor the response executor
     * @param addressResponse  the address response
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public void tryToSend(ExecutorService responseExecutor, AddressResponse addressResponse) {
        responseExecutor.submit(() -> {
            try {
                sendPackage(addressResponse);
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Произошла ошибка при отправке ответа: " + e.getMessage());
            }
        });
    }
}
