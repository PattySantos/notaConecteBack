package br.com.nfse.nfse_saas.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
    private static final int MIN_SECRET_LENGTH = 32;
    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(@Value("${auth.jwt.secret}") String secret,
                      @Value("${auth.jwt.expiration-minutes}") long expirationMinutes) {
        if (secret == null || secret.length() < MIN_SECRET_LENGTH) {
            throw new IllegalStateException("JWT_SECRET deve possuir pelo menos 32 caracteres");
        }
        if (expirationMinutes < 5 || expirationMinutes > 1440) {
            throw new IllegalStateException("JWT_EXPIRATION_MINUTES deve estar entre 5 e 1440");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String gerar(String email) {
        Instant agora = Instant.now();
        return Jwts.builder().subject(email).issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(key).compact();
    }

    public String obterEmail(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }
}
