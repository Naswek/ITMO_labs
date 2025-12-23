package org.example.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@ApplicationScoped
public class JwtUtils {

    private static final String SECRET = "предположим_супер_секректный_ключ";
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private final long ACCESS_EXPIRATION = 15 * 60_000;
    private final long REFRESH_EXPIRATION = 7 * 24 * 60 * 60_000;

    public String generateAccessToken(long userId, String login) {
        return Jwts.builder()
                .setSubject(login)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ACCESS_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateRefreshToken(Long userId, String login) {
        return Jwts.builder()
                .setSubject(login)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Некорректный токен: " + e.getMessage()); //пока так, потом логгер доавблю
        } catch (ExpiredJwtException e) {
            System.err.println("Токен просрочен: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Неподдерживаемый токен: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Пустой токен: " + e.getMessage());
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }

    public long getACCESS_EXPIRATION() {
        return ACCESS_EXPIRATION;
    }

    public long getREFRESH_EXPIRATION() {
        return REFRESH_EXPIRATION;
    }
}
