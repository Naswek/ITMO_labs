package org.example.api;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.dto.AuthResultWithUser;
import org.example.entity.UserEntity;
import org.example.enums.AuthResult;
import org.example.utils.cdi.UserService;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    static final String SESSION_USER_ID = "userId";

    @Inject
    private UserService service;

    @Context
    private HttpServletRequest request;

    @POST
    public Response register(Credentials credentials) {
        requireCredentials(credentials);
        UserEntity user = service.createUser(credentials.getLogin(), credentials.getPassword());
        return Response.status(Response.Status.CREATED)
                .entity(new UserView(user.getId(), user.getLogin()))
                .build();
    }

    @POST
    @Path("/login")
    public Response login(Credentials credentials) {
        requireCredentials(credentials);
        AuthResultWithUser result = service.checkUser(credentials.getLogin(), credentials.getPassword());
        if (result.authResult() != AuthResult.SUCCESS) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiError.of("Неверный логин или пароль", "INVALID_CREDENTIALS"))
                    .build();
        }

        HttpSession session = request.getSession(true);
        request.changeSessionId();
        session.setAttribute(SESSION_USER_ID, result.userId());
        return Response.ok(new UserView(result.userId(), result.login())).build();
    }

    @POST
    @Path("/logout")
    public Response logout() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/me")
    public Response me() {
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute(SESSION_USER_ID) instanceof Long userId)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiError.of("Для выполнения операции войдите в систему", "UNAUTHORIZED"))
                    .build();
        }

        UserEntity user = service.getUserById(userId);
        return Response.ok(new UserView(user.getId(), user.getLogin())).build();
    }

    private static void requireCredentials(Credentials credentials) {
        if (credentials == null) {
            throw new IllegalArgumentException("Укажите логин и пароль");
        }
    }

    public record UserView(Long id, String login) {
    }

    public static class Credentials {
        private String login;
        private String password;

        public Credentials() {
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
    }
}
