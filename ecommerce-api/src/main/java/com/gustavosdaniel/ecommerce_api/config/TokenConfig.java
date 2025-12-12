package com.gustavosdaniel.ecommerce_api.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gustavosdaniel.ecommerce_api.user.JWTUser;
import com.gustavosdaniel.ecommerce_api.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Component
public class TokenConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(User user) {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("UserId", user.getId().toString())
                .withClaim("role", user.getUserRole().name())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusMillis(expiration))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUser> validation(String token){

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT decoderJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);

            String userId = decoderJWT.getClaim("UserId").asString();
            String email = decoderJWT.getSubject();
            String role = decoderJWT.getClaim("role").asString();

            if (email == null || userId == null) {
                return Optional.empty();
            }

            UUID userIdConverted = UUID.fromString(userId);

            JWTUser jwtUser = new JWTUser(userIdConverted, email, role);

            return Optional.of(jwtUser);

        }catch (Exception e){

            return Optional.empty();
        }
    }
}
