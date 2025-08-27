package org.example.backend.global.auth.util;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.global.auth.exception.InvalidJwtException;
import org.example.backend.global.auth.exception.JwtExpiredException;
import org.example.backend.global.auth.exception.JwtNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Component
public class JwtUtil {
    private final SecretKey secretKey;

    @Value("${secret.jwt-expired-in}")
    private long accessTokenExpireMs;

    public JwtUtil(@Value("${secret.jwt-secret-key}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String createAccessToken(String email, Long userId) {
        return Jwts.builder()
                .claim(JwtClaimKey.EMAIL.getKey(), email)
                .claim(JwtClaimKey.USER_ID.getKey(), userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpireMs))
                .signWith(secretKey)
                .compact();
    }

    public Claims validateJwt(String token){
        log.info("validateJwt");
        try{
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (MalformedJwtException e) {
            throw new InvalidJwtException(INVALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException(EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            throw new InvalidJwtException(INVALID_JWT);
        } catch (IllegalArgumentException e) {
            throw new JwtNotFoundException(JWT_NOT_FOUND);
        }
    }
}