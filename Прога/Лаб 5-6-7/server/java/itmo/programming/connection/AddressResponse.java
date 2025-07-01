package itmo.programming.connection;

import itmo.programming.responses.Response;
import java.net.InetSocketAddress;

/**
 * The type Address response.
 */
public record AddressResponse(InetSocketAddress socketAddress, Response response) {
}
