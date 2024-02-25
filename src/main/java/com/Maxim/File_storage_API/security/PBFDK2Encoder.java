package com.Maxim.File_storage_API.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PBFDK2Encoder implements PasswordEncoder {

    @Value("${jwt.secret_encoder}")
    private String secret;

    @Value("${jwt.iteration_encoder}")
    private Integer iteration;

    @Value("${jwt.keylength_encoder}")
    private Integer keyLength;

    private static final String SECRET_KEY_INSTANCE = "PBKDF2WithHmacSHA512";

    @Override
    public String encode(CharSequence rawPassword) {

        try {
            byte [] result = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE)
                    .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(),
                            secret.getBytes(), iteration, keyLength))
                    .getEncoded();
            return Base64.getEncoder()
                    .encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}