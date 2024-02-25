package com.Maxim.File_storage_API.security;


import com.Maxim.File_storage_API.entity.Status;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.exceptions.security_exeptions.InvalidCredentialsException;
import com.Maxim.File_storage_API.exceptions.security_exeptions.UserNotAuthenticatedException;
import com.Maxim.File_storage_API.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    @Value("${jwt.issuer}")
    private String issuer;

    public SecurityService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    private TokenDetails generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.getRole());
            put("username", user.getName());
        }};
        return generateToken(claims, user.getId().toString());
    }

    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        Long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);
        return generateToken(expirationDate, claims, subject);
    }


    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        TokenDetails tokenDetails = new TokenDetails();
        tokenDetails.setToken(token);
        tokenDetails.setIssuedAt(createdDate);
        tokenDetails.setExpiresAt(expirationDate);
        return tokenDetails;
    }

    public Mono<TokenDetails> authenticate(String username, String password) {
        return userService.getUserByUsername(username)
                .flatMap(user -> {
                    if (user.getStatus().equals(Status.DELETED)) {
                        return Mono.error(new UserNotAuthenticatedException("User deleted, authenticate is not possible"));
                    }
                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new InvalidCredentialsException("Invalid password"));
                    }
                    TokenDetails tokenDetails = generateToken(user);
                    tokenDetails.setUserId(user.getId());
                    return Mono.just(tokenDetails);
                })
                .switchIfEmpty(Mono.error(new InvalidCredentialsException("Invalid username")));
    }




}
