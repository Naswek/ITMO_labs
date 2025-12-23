package org.example.services;

import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

@Stateless
public class EmailService {

    public void sendEmail(String to, String subject, String body) {
        try {
            String from = System.getenv("SMTP_USERNAME");
            if (from == null || from.isEmpty()) {
                throw new IllegalStateException("Переменная окружения SMTP_USERNAME не задана");
            }

            Session session = createMailSession(from);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);


            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Не удалось отправить email: " + e.getMessage(), e);
        }
    }

    public void sendPasswordHint(String to, String hint) {
        String subject = "Подсказка для пароля";
        String body = "Ваша подсказка для восстановления пароля:\n\n" + hint + "\n\n" +
                "Если вы не запрашивали подсказку, просто проигнорируйте это письмо.";
        sendEmail(to, subject, body);
    }

    private Session createMailSession(String from) {

        String password = System.getenv("SMTP_PASSWORD");
        if (password == null || password.isEmpty()) {
            throw new IllegalStateException("Переменная окружения SMTP_PASSWORD не задана");
        }

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
    }
}
