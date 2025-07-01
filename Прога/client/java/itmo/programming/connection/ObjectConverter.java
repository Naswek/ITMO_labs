package itmo.programming.connection;

import itmo.programming.requests.Request;
import itmo.programming.responses.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;


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
     * Deserialize response response.
     *
     * @param data the data
     * @return the response
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     * @throws ClassCastException     the class cast exception
     */
    public Response deserializeResponse(byte[] data) throws
            IOException,
            ClassNotFoundException,
            ClassCastException {

        if (data == null || data.length == 0) {
            throw new IOException("Получены пустые данные");
        }

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {

            objectInputStream.setObjectInputFilter(filter);
            return (Response) objectInputStream.readObject();
        }
    }

    /**
     * Serialize request.
     *
     * @param request          the request
     * @param byteBufferOutput the byte buffer output
     * @throws IOException the io exception
     */
    public void serializeRequest(Request request, ByteBuffer byteBufferOutput)
            throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream
                     = new ObjectOutputStream(byteArrayOutputStream);) {


            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            byteBufferOutput.clear();
            byteBufferOutput.put(byteArrayOutputStream.toByteArray());
            byteBufferOutput.flip();
        }
    }
}
