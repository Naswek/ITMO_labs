package org.example.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.entity.PendingRegistrationEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.ExpiredTokenException;
import org.example.exceptions.InvalidTokenException;
import org.example.exceptions.NotUniqueException;
import org.example.repository.RegistrationRepository;
import org.example.repository.UserRepository;
import org.example.security.PasswordEncoder;
import org.example.utils.Email;

import java.time.LocalDateTime;
import java.util.UUID;

@Stateless
public class RegistrationService {

    @EJB
    private RegistrationRepository newUserRepository;

    @EJB
    private UserRepository userRepository;

    @Inject
    private EmailService emailService;

    @Inject
    private PasswordEncoder encoder;

    public void requestRegistration(UserEntity user) {

        String email = user.getEmail().getValue();

        cleanupExpiredTokens();

        final String token = UUID.randomUUID().toString();
        final PendingRegistrationEntity pendingRegistration = createPendingRegistration(token, user);


        final String link = "http://localhost:8080/api/auth/confirm?token=" + token;
        final String subject = "Подтвердите регистрацию";
        emailService.sendEmail(email, subject, link);

        newUserRepository.save(pendingRegistration);
    }


    public void completeRegistration(String token) throws InvalidTokenException,
                                                            ExpiredTokenException,
                                                            NotUniqueException {
        PendingRegistrationEntity pending = newUserRepository.findPendingByToken(token);

        if (pending == null) {
            throw new InvalidTokenException("Неверный токен, попробуйте зарегистрироваться заново");
        }

        if (pending.isUsed()) {
            newUserRepository.deleteUsed();
            throw new InvalidTokenException("Токен уже использован, попробуйте зарегистрироваться заново");
        }

        UserEntity user = createUserEntity(pending);
        userRepository.save(user);
        pending.setUsed(true);
    }

    @Schedule(minute = "*/30", persistent = false)
    public void cleanupExpiredTokens() {
        LocalDateTime tenHoursAgo = LocalDateTime.now().minusHours(10);
        newUserRepository.deleteExpired(tenHoursAgo);
    }

    private PendingRegistrationEntity createPendingRegistration(String token, UserEntity user) {
        final var pending = new PendingRegistrationEntity();
        pending.setLogin(user.getLogin());
        pending.setCreatedAt(LocalDateTime.now());
        pending.setToken(token);
        pending.setPassword(encoder.encode(user.getPassword())) ;
        pending.setEmail(user.getEmail().getValue());
        pending.setPasswordHint(user.getPasswordHint());
        return pending;
    }

    private UserEntity createUserEntity(PendingRegistrationEntity pending) {
        final var user = new UserEntity();
        user.setEmail(new Email(pending.getEmail()));
        user.setLogin(pending.getLogin());
        user.setPassword(pending.getPassword());
        user.setPasswordHint(pending.getPasswordHint());
        return user;
    }
}
