package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.dto.AuthResultWithUser;
import org.example.entity.UserEntity;
import org.example.enums.AuthResult;
import org.example.utils.cdi.validators.UserValidator;
import org.example.utils.repository.UserRepository;
import org.example.utils.security.PasswordEncoder;

@ApplicationScoped
public class UserService {

    @Inject
    private UserRepository repository;

    @Inject
    private PasswordEncoder encoder;

    @Inject
    private UserValidator validator;

    public AuthResultWithUser checkUser(String login, String password) {
        String normalizedLogin = validator.normalizeLogin(login);
        validator.validatePassword(password);

        UserEntity user = repository.findByLogin(normalizedLogin);
        if (user == null) {
            return new AuthResultWithUser(AuthResult.USER_NOT_FOUND, null, null);
        }
        if (!encoder.matches(password, user.getPasswordHash())) {
            return new AuthResultWithUser(AuthResult.WRONG_PASSWORD, null, null);
        }
        return new AuthResultWithUser(AuthResult.SUCCESS, user.getId(), user.getLogin());
    }

    @Transactional
    public UserEntity createUser(String login, String password) {
        String normalizedLogin = validator.normalizeLogin(login);
        validator.validatePassword(password);

        if (repository.findByLogin(normalizedLogin) != null) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        UserEntity user = new UserEntity();
        user.setLogin(normalizedLogin);
        user.setPasswordHash(encoder.encode(password));
        return repository.create(user);
    }

    public UserEntity getUserById(Long userId) {
        UserEntity user = repository.findById(userId);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь с ID " + userId + " не найден");
        }
        return user;
    }

}
