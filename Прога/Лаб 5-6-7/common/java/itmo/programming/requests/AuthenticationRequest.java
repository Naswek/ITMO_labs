package itmo.programming.requests;

import java.io.Serializable;

/**
 * The type Authentication request.
 */
public record AuthenticationRequest(String user, String hashPasswrd, boolean isRegistred) implements
        Request,
        Serializable {
}
