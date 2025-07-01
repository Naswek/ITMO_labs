package itmo.programming.connection;

import itmo.programming.requests.Request;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * The type Object converter.
 */
public class ObjectConverter {

    private final ObjectInputFilter filter;

    /**
     * Instantiates a new Object converter.
     */
    public ObjectConverter() {
        this.filter = ObjectInputFilter.Config.createFilter("java.lang.String;java.util.List; !*");
    }

    /**
     * Deserialize request address request.
     *
     * @param requestPacket the request packet
     * @return the address request
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public AddressRequest deserializeRequest(DatagramPacket requestPacket) throws
            IOException,
            ClassNotFoundException {

        final byte[] data = requestPacket.getData();
        final InetAddress inetAddress = requestPacket.getAddress();
        final int port = requestPacket.getPort();


        try (
                var byteArrayInputStream = new ByteArrayInputStream(
                        data,
                        0,
                        requestPacket.getLength());
                var objectInputStream = new ObjectInputStream(
                        byteArrayInputStream);) {
            objectInputStream.setObjectInputFilter(filter);
            return new AddressRequest(
                    new InetSocketAddress(inetAddress, port),
                    (Request) objectInputStream.readObject());
        }
    }

    /**
     * Serialize response datagram packet.
     *
     * @param address the address
     * @return the datagram packet
     * @throws IOException        the io exception
     * @throws ClassCastException the class cast exception
     */
    public DatagramPacket serializeResponse(AddressResponse address)
            throws IOException, ClassCastException {
        final var byteArrayOutputStream = new ByteArrayOutputStream();
        final var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(address.response());
        final byte[] sendBuffer = byteArrayOutputStream.toByteArray();

        return new DatagramPacket(
                sendBuffer,
                sendBuffer.length,
                address.socketAddress().getAddress(),
                address.socketAddress().getPort());
    }
}
