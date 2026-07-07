package com.dishd.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Geracao e validacao de tokens JWT (HS256). */
@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${dishd.jwt.secret}") String secret,
                      @Value("${dishd.jwt.expiration-ms}") long expirationMs) {
        if (secret.startsWith("troque-esta-chave")) {
            log.warn("JWT usando o secret PADRAO de desenvolvimento. "
                    + "Defina a variavel de ambiente DISHD_JWT_SECRET em producao.");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /** Gera um token assinado para o usuario (subject = e-mail, claim "uid" = id). */
    public String generateToken(String email, Long usuarioId) {
        Instant agora = Instant.now();
        return Jwts.builder()
                .subject(email)
                .claim("uid", usuarioId)
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plusMillis(expirationMs)))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return parse(token).getSubject();
    }

    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
