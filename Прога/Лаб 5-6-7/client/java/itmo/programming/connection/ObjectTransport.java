package itmo.programming.connection;

import itmo.programming.PrettyColor;
import itmo.programming.connection.visitors.VisitorDispatch;
import itmo.programming.exceptions.ExitCommandException;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.message.OutputMessage;
import itmo.programming.requests.AuthenticationRequest;
import itmo.programming.requests.Request;
import itmo.programming.responses.ValidationResponse;
import itmo.programming.util.ClientControlCommand;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * The type Object transport.
 */
public class ObjectTransport {

    private final DatagramChannel channel;
    private final InetSocketAddress address;
    private ClientControlCommand commandManager;
    private final ByteBuffer byteBufferOutput;
    private final ByteBuffer byteBufferInput;
    private final int bufferSize = 65507;
    private final ObjectConverter converter;

    /**
     * Instantiates a new Object transport.
     *
     * @param channel the channel
     * @param address the address
     */
    public ObjectTransport(DatagramChannel channel,
                           InetSocketAddress address) {
        this.commandManager = null;
        this.converter = new ObjectConverter();
        this.byteBufferOutput = ByteBuffer.allocate(bufferSize);
        this.byteBufferInput = ByteBuffer.allocate(bufferSize);
        this.channel = channel;
        this.address = address;
    }

    /**
     * Send package output message.
     *
     * @param input the input
     * @return the output message
     * @throws IOException            the io exception
     * @throws ExitCommandException   the exit command exception
     * @throws FailedExecution        the failed execution
     * @throws ClassNotFoundException the class not found exception
     */
    public OutputMessage sendPackage(String input) throws
            IOException,
            ExitCommandException,
            FailedExecution,
            ClassNotFoundException {

        converter.serializeRequest(commandManager.convertInput(input),
                byteBufferOutput);

        send();
        return receivePackage();
    }


    /**
     * Receive package output message.
     *
     * @return the output message
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     * @throws FailedExecution        the failed execution
     */
    public OutputMessage receivePackage() throws
            IOException, ClassNotFoundException, FailedExecution {
        final long startTime = System.currentTimeMillis();
        int attempts = 10;
        final int timeout = 5000;
        final int timeSleep = 100;


        while (attempts-- > 0) {
            byteBufferInput.clear();
            final InetSocketAddress serverAddress
                    = (InetSocketAddress) channel.receive(byteBufferInput);

            if (serverAddress != null) {
                byteBufferInput.flip();
                final byte[] receiveData = new byte[byteBufferInput.remaining()];
                byteBufferInput.get(receiveData);
                final var responseVisitor = new VisitorDispatch(ObjectTransport.this);
                return responseVisitor.dispatchVisitor(converter.deserializeResponse(receiveData));
            }

            if (System.currentTimeMillis() - startTime > timeout) {
                break;
            }

            try {
                Thread.sleep(timeSleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return new OutputMessage(PrettyColor.red("Сервер не ответил после нескольких попыток"));
    }

    /**
     * Send package with param string.
     *
     * @param response the response
     * @return the string
     * @throws FailedExecution        the failed execution
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public String sendPackageWithParam(ValidationResponse response) throws
            FailedExecution,
            IOException,
            ClassNotFoundException {
        converter.serializeRequest(commandManager.createRequest(response), byteBufferOutput);
        send();
        return receivePackage().message();
    }

    /**
     * Send package with user data output message.
     *
     * @return the output message
     * @throws IOException            the io exception
     * @throws FailedExecution        the failed execution
     * @throws ClassNotFoundException the class not found exception
     */
    public OutputMessage sendPackageWithUserData(Request input) throws
            IOException, FailedExecution, ClassNotFoundException {

        converter.serializeRequest((input), byteBufferOutput);

        send();
        return receivePackage();
    }

    private void send() throws IOException {
        channel.send(byteBufferOutput, address);
    }

    /**
     * Sets command control.
     *
     * @param commandControl the command control
     */
    public void setCommandControl(ClientControlCommand commandControl) {
        this.commandManager = commandControl;
    }
}
