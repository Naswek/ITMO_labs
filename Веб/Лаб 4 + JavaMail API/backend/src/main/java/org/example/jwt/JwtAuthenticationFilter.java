package org.example.jwt;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.List;

@Provider
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private JwtUtils jwtUtils;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/refresh",
            "/auth/password-hint",
            "/auth/confirm"
    );

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String path = requestContext.getUriInfo().getPath();

        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            return;
        }

        String accessToken = getCookieValue(requestContext, "access_token");

        if (accessToken == null || jwtUtils.isTokenExpired(accessToken)) {
            requestContext.abortWith(Response.status(401).build());
            return;
        }

        Claims claims = jwtUtils.parseToken(accessToken);
        requestContext.setProperty("currentUserId", claims.get("userId", Long.class));
    }

    private String getCookieValue(ContainerRequestContext ctx, String name) {
        List<String> cookies = ctx.getHeaders().get("Cookie");
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
}
