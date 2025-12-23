package org.example.security;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class PasswordEncoder {

    public String encode(String password) {
        final String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(password, salt);
    }

    public boolean matches(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
