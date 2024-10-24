package com.example.polynomial.exception;


public class JwtAuthenticationFilterException extends RuntimeException {

    public JwtAuthenticationFilterException(String message, Throwable cause) {
        super(message, cause);
    }
}
