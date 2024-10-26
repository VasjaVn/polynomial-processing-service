package com.example.polynomial.exception;


public class MissingJwtTokenException extends RuntimeException {

    public MissingJwtTokenException(String message) {
        super(message);
    }
}
