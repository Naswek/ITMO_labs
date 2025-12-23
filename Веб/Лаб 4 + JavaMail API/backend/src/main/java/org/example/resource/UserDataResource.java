package org.example.resource;

import io.jsonwebtoken.Claims;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.exceptions.InvalidInputException;
import org.example.exceptions.UnauthorizedException;
import org.example.jwt.JwtUtils;
import org.example.repository.ResultRepository;
import org.example.services.ResultService;
import org.example.utils.Validation;
import org.example.repository.UserRepository;
import org.example.entity.ResultEntity;
import org.example.entity.UserEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Path("/index")
@Produces(MediaType.APPLICATION_JSON)
public class UserDataResource {

    @EJB
    private transient ResultRepository resultRepository;

    @Inject
    private transient ResultService resultService;

    @Context
    private HttpHeaders httpHeaders;
    @Inject
    private JwtUtils jwtUtils;

    private static final List<Double> VALID_VALUES =
            Arrays.asList(-2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0);


    @POST
    @Path("/submit")
    public Response save(@FormParam("xValue") Double xValues,
                                @FormParam("yValue") Double yValue,
                                @FormParam("rValue") Double rValues) {

        validateValues(xValues, yValue, rValues);

        final UserEntity user = getCurrentUser();
        resultService.savePoints(rValues, xValues, yValue, user);
        return Response.ok().build();
    }

    @DELETE
    @Path("/clear")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clear() {
        final UserEntity user = getCurrentUser();
        resultRepository.clear(user.getId());
        return Response.ok().build();
    }

    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getData() {
        var results = resultRepository.findAll();
        return Response.ok(results).build();
    }

    @GET
    @Path("/check-auth")
    public Response checkAuth() {
        return Response.ok().build();
    }

    private UserEntity getCurrentUser() {
        String accessToken = getCookieValue("access_token");

        if (accessToken == null || jwtUtils.isTokenExpired(accessToken)) {
            throw new UnauthorizedException("Неавторизован");
        }

        Claims claims = jwtUtils.parseToken(accessToken);
        Long userId = claims.get("userId", Long.class);
        return resultService.getUser(userId);
    }

    private String getCookieValue(String name) {
        var cookies = httpHeaders.getRequestHeader("Cookie");
        if (cookies == null) return null;
        for (String cookie : cookies) {
            for (String part : cookie.split(";")) {
                String[] pair = part.trim().split("=");
                if (pair.length == 2 && name.equals(pair[0])) {
                    return pair[1];
                }
            }
        }
        return null;
    }

    public void validateValues(Double x, Double y, Double r) {
        if (!(x >= -2 && x <= 2)) {
            throw new InvalidInputException("Недопустимое значение X");
        }
        if (!VALID_VALUES.contains(r)) {
            throw new InvalidInputException("Недопустимое значение R");
        }
        if (!(y > -5 && y < 3)) {
            throw new InvalidInputException("Y должен быть в диапазоне (-5; 3)");
        }
    }
}