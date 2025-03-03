package ru.malik.spring.SprinSecurity.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.time.ZonedDateTime;


@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    public String generateJWTToken(String username) {
        Date experationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("malik")
                .withExpiresAt(experationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaims(String token) throws JWTVerificationException {
        JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("malik")
                .build();

        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("username").asString();
    }
}
