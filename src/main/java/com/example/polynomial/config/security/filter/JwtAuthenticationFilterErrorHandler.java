package com.example.polynomial.config.security.filter;

import com.example.polynomial.exception.JwtAuthenticationFilterException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilterErrorHandler {

    public void handleError() {
        Throwable throwable = JwtAuthenticationFilterExceptionHolder.getAndClear();
        if (throwable != null) {
            throw new JwtAuthenticationFilterException("Jwt authentication process failure", throwable);
        }
    }
}
