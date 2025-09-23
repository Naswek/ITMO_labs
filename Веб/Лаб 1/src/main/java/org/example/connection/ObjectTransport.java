package org.example.connection;

import com.fastcgi.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;

/**
 * The type Object transport.
 */
public class ObjectTransport {

    private final ObjectConverter converter;
    private static final Logger log = LogManager.getLogger(ObjectTransport.class);


    /**
     * Instantiates a new Object transport.
     *
     * @param converter the converter
     */
    public ObjectTransport(ObjectConverter converter)  {
        this.converter = converter;

    }

    /**
     * Send package.
     *
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    private void sendPackage(String query)
            throws IOException {
        System.out.println(converter.serializeResponse(query));
    }

    public void receiveAndSend() {
        try {
            var fcgiInterface = new FCGIInterface();
            while (fcgiInterface.FCGIaccept() >= 0) {
                var method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
                if (method.equals("GET")) {
                    String query = FCGIInterface.request.params.getProperty("QUERY_STRING");
                    log.info(query);
                    sendPackage(query);
                } else {
                    System.out.println("Status: 405 Method Not Allowed\r\n" +
                            "Content-type: text/html\r\n\r\n" +
                            "<html><head><title>405 Method Not Allowed</title></head>" +
                            "<body><h1>405 Method Not Allowed</h1>" +
                            "<p>The requested method " + method + " is not allowed.</p>" +
                            "</body></html>");
                }
            }
        } catch (IOException e) {
            log.error("Произошла ошибка на сервере: " + e.getMessage());
        }
    }
}
