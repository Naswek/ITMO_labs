package org.example.resource;

import io.jsonwebtoken.Claims;
import jakarta.inject.Inject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.example.exceptions.*;
import org.example.entity.UserEntity;
import org.example.jwt.JwtUtils;

import jakarta.validation.Validator;
import org.example.security.PasswordEncoder;
import org.example.services.RegistrationService;
import org.example.services.UserService;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService userService;

    @Inject
    private JwtUtils jwtUtils;

    @Inject
    private RegistrationService registrationService;


    @POST
    @Path("/register")
    public Response register( // вот здесь нужно еще дописать метод, проверяющий уникальность данных для незареганных юезров
                              // при повтором вводе регистрации нету оповещения + падает на бдшке, тк данные не уникаьлные --- надо тестить
                              //сделать веб сокет
                              //надо бы на фронт добавить отображение логина на /main странице
            @FormParam("login") String login,
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("passwordHint") String passwordHint) {


        validateRegistrationInput(login, email, password);
        UserEntity user = createUser(login, email, password, passwordHint);
        registrationService.requestRegistration(user);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    public Response authenticateUser(@FormParam("login") String login,
                                     @FormParam("email") String email,
                                     @FormParam("password") String password) {

        String identifier = Optional.ofNullable(login).orElse(email);
        if (identifier == null) {
            return Response.status(400).entity(Map.of("error", "Не указан логин или email")).build();
        }

        boolean isLogin = login != null;
        AuthResultWithUser result = userService.checkUser(identifier, password, isLogin);
        return handleAuthResult(result);
    }

    @GET
    @Path("/confirm")
    public Response confirmRegistration(@QueryParam("token") String token) {
        if (token == null || token.trim().isEmpty()) {
            return Response.status(400).entity("Токен не указан").build();
        }

        registrationService.completeRegistration(token);
        return Response.seeOther(URI.create("http://localhost/login?confirmed=true")).build();
    }

    @POST
    @Path("/password-hint")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response requestPasswordHint(@FormParam("identifier") String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new InvalidInputException("Укажите email или логин");
        }

        boolean isLogin = !identifier.contains("@");
        userService.sendPasswordHint(identifier, isLogin);

        return Response.ok(Map.of(
                "message", "Если аккаунт существует, подсказка отправлена на email."
        )).build();
    }

    @GET
    @Path("/check-auth")
    public Response checkAuth(@Context HttpServletRequest request) {
        String accessToken = getCookieValue(request, "access_token");

        if (accessToken == null || jwtUtils.isTokenExpired(accessToken)) {
            throw new UnauthorizedException("Неавторизован");
        }

        Claims claims = jwtUtils.parseToken(accessToken);
        Long userId = claims.get("userId", Long.class);
        String login = claims.getSubject();

        return Response.ok(Map.of(
                "userId", userId,
                "login", login
        )).build();
    }

    @POST
    @Path("/refresh")
    public Response refresh(@Context HttpServletRequest request) {
        String refreshToken = getCookieValue(request, "refresh_token");

        if (refreshToken == null || jwtUtils.isTokenExpired(refreshToken)) {
            throw new UnauthorizedException("Неавторизован");
        }

        Claims claims = jwtUtils.parseToken(refreshToken);
        Long userId = claims.get("userId", Long.class);
        String login = claims.getSubject();

        String newAccessToken = jwtUtils.generateAccessToken(userId, login);
        String newRefreshToken = jwtUtils.generateRefreshToken(userId, login);

        return Response.ok()
                .header("Set-Cookie", buildCookie("access_token", newAccessToken, jwtUtils.getACCESS_EXPIRATION()))
                .header("Set-Cookie", buildCookie("refresh_token", newRefreshToken, jwtUtils.getREFRESH_EXPIRATION()))
                .build();
    }

    @POST
    @Path("/logout")
    public Response logout() {
        String clearAccess = buildCookie("access_token", "", 0);
        String clearRefresh = buildCookie("refresh_token", "", 0);
        return Response.ok()
                .header("Set-Cookie", clearAccess)
                .header("Set-Cookie", clearRefresh)
                .build();
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private UserEntity createUser(String login, String email, String password, String passwordHint) {
        final UserEntity user = new UserEntity();
        user.setEmail(new org.example.utils.Email(email));
        user.setLogin(login);
        user.setPasswordHint(passwordHint);
        user.setPassword(password);
        return user;
    }

    private Response handleAuthResult(AuthResultWithUser result) {
        return switch (result.authResult()) {
            case SUCCESS -> successResponse(result.user());
            case USER_NOT_FOUND -> throw new UnauthorizedException("Пользователь не найден");
            case WRONG_PASSWORD -> throw new UnauthorizedException("Неверный пароль");
            default -> throw new ApiException(jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR,
                    "INTERNAL_ERROR", "Internal server error") {};
        };
    }

    private Response successResponse(UserEntity user) {
        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getLogin());
        String refreshToken = jwtUtils.generateRefreshToken(user.getId(), user.getLogin());

        return Response.ok()
                .header("Set-Cookie", buildCookie("access_token", accessToken, jwtUtils.getACCESS_EXPIRATION()))
                .header("Set-Cookie", buildCookie("refresh_token", refreshToken, jwtUtils.getREFRESH_EXPIRATION()))
                .build();
    }

    private void validateRegistrationInput(String login, String email, String password) throws InvalidInputException{

        if ((login == null || login.trim().isEmpty()) &&
                (email == null || email.trim().isEmpty())) {
            throw new InvalidInputException("Логин или email не могут быть пустыми");
        }

        if (password == null || password.length() < 6) {
            throw new InvalidInputException("Пароль должен содержать не менее 6 символов");
        }

        if (email != null && !email.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,}$")) {
            throw new InvalidInputException("Некорректный формат email");
        }
    }

    private String buildCookie(String name, String value, long maxAgeMs) {
        return String.format(
                "%s=%s; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=%d",
                name, value, maxAgeMs / 1000
        );
    }
}