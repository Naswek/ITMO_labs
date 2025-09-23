package org.example;

import org.example.connection.ObjectConverter;
import org.example.connection.ObjectTransport;

import java.io.IOException;
import java.net.SocketException;

/**
 * The type Server.
 */
public class Server {

    private final ObjectTransport objectTransport;

    public Server() throws IOException {
        this.objectTransport = new ObjectTransport(new ObjectConverter());
    }

    /**
     * Start.
     */
    public void start() {
        while (true) {
            objectTransport.receiveAndSend();
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {



        try {
            final var server = new Server();
            server.start();

        } catch ( IOException e) {
            System.err.println("Порт должен быть числом: " + args[1]);
        }
    }
}
