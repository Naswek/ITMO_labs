package org.example.entity;

import jakarta.persistence.*;
import org.example.converter.EmailConverter;
import org.example.utils.Email;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "password_hint", nullable = false)
    private String passwordHint;

    @Convert(converter = EmailConverter.class)
    @Column(name = "email", nullable = false)
    private Email email;

    public UserEntity() {};

    public UserEntity(String login, String password, Email email, String passwordHint) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.passwordHint = passwordHint;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHint() {
        return passwordHint;
    }
    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public Email getEmail() {
        return email;
    }
    public void setEmail(Email email) {
        this.email = email;
    }
}
