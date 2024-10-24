package com.example.polynomial.util.validator;

import com.example.polynomial.config.app.props.JwtProperties;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;

@Log4j2
@Component
public class JwtTokenValidator {

    private final JwtProperties jwtProperties;

    public JwtTokenValidator(JwtProperties jwtProperties) {
        Assert.isTrue(jwtProperties != null &&
                        jwtProperties.getSecretKey() !=null &&
                        !jwtProperties.getSecretKey().isEmpty(),
                "Signing key cannot be null or empty.");

        this.jwtProperties = jwtProperties;
    }

    public boolean validate(String jwtToken) {
        log.info("Validation process for JWT Token");

        Date expiration = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKey().getBytes())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .getExpiration();

        return expiration.after(new Date());
    }
}
