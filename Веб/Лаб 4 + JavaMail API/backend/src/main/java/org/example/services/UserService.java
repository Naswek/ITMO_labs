package org.example.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import org.example.entity.UserEntity;
import org.example.enums.AuthResult;
import org.example.jwt.JwtUtils;
import org.example.repository.UserRepository;
import org.example.resource.AuthResultWithUser;
import org.example.security.PasswordEncoder;

@Stateless
public class UserService {

    @EJB
    private UserRepository repository;

    @Inject
    private PasswordEncoder encoder;

    @Inject
    private EmailService emailSender;

    @Inject
    private JwtUtils jwtUtils;

    public AuthResultWithUser checkUser(String value, String password, boolean isLogin) {
        try {
            UserEntity user = isLogin ? repository.findByLogin(value) : repository.findByEmail(value);
            if (user == null) {
                return new AuthResultWithUser(AuthResult.USER_NOT_FOUND, null, null);
            }
            if (encoder.matches(password, user.getPassword())) {
                System.out.println("Checking user: " + value);
                System.out.println("Password matches: " + encoder.matches(password, user.getPassword()));

                String token = jwtUtils.generateAccessToken(user.getId(), user.getLogin());
                return new AuthResultWithUser(AuthResult.SUCCESS, user, token);
            } else {
                return new AuthResultWithUser(AuthResult.WRONG_PASSWORD, user, null);
            }
        } catch (NoResultException e) {
            return new AuthResultWithUser(AuthResult.USER_NOT_FOUND, null, null);
        }
    }

    public void sendPasswordHint(String identifier, boolean isLogin) {
        UserEntity user = isLogin
                ? repository.findByLogin(identifier)
                : repository.findByEmail(identifier);

        if (user != null && user.getPasswordHint() != null && !user.getPasswordHint().isEmpty()) {
            emailSender.sendPasswordHint(user.getEmail().getValue(), user.getPasswordHint());
        }
    }

    public UserEntity getUserById(Long userId) {
        return repository.findById(userId);
    }
}
