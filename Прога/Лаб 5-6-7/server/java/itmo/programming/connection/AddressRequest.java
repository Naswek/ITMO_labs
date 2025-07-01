package itmo.programming.connection;

import itmo.programming.requests.Request;
import java.net.InetSocketAddress;

/**
 * The type Address request.
 */
public record AddressRequest(InetSocketAddress socketAddress, Request request) {
}
