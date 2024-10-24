package com.example.polynomial.controller.advice;

import com.example.polynomial.exception.ConverterUnexpectedCaseException;
import com.example.polynomial.exception.JwtAuthenticationFilterException;
import com.example.polynomial.exception.PolynomialNotFoundException;
import com.example.polynomial.model.dto.response.ErrorResponseDto;
import io.lettuce.core.RedisException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> details.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(BAD_REQUEST)
                .body(toErrorResponseDto("Request params are not valid", details));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(toErrorResponseDto("Missing request parameter",
                        Map.of("detailMessage", ex.getMessage())));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(toErrorResponseDto("Request parameter with name '" + ex.getName() + "' has wrong type",
                        Map.of("detailMessage", ex.getMessage())));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(toErrorResponseDto("Request param is not valid",
                        Map.of("detailMessage", ex.getMessage())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(toErrorResponseDto("Request params are not valid",
                        Map.of("detailMessage", ex.getMessage())));
    }

    @ExceptionHandler(PolynomialNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePolynomialNotFoundException(PolynomialNotFoundException ex) {
        return ResponseEntity.status(NOT_FOUND)
                .body(toErrorResponseDto(ex.getMessage(), null));
    }

    @ExceptionHandler(JwtAuthenticationFilterException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtAuthenticationFilterException(JwtAuthenticationFilterException ex) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(toErrorResponseDto(ex.getMessage(), Map.of("detailMessage", ex.getCause().getMessage())));
    }

    @ExceptionHandler(ConverterUnexpectedCaseException.class)
    public ResponseEntity<ErrorResponseDto> handleConverterUnexpectedCaseException(ConverterUnexpectedCaseException ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(toErrorResponseDto("Converter unexpected case", null));
    }

    @ExceptionHandler(RedisException.class)
    public ResponseEntity<ErrorResponseDto> handleRedisException(RedisException ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(toErrorResponseDto("Internal Server Error: Redis: " + ex.getMessage(),
                        Map.of("detailMessage", ex.getCause().getMessage())));
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponseDto> handleConnectException(ConnectException ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(toErrorResponseDto("Internal Server Error: Can't connect to DataBase.",
                        Map.of("detailMessage", ex.getMessage())));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponseDto> handleSQLException(SQLException ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(toErrorResponseDto("Internal Server Error: Error during communication with DataBase",
                        Map.of("detailMessage", ex.getMessage().split("\\n")[0])));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(toErrorResponseDto("Internal Server Error",
                        Map.of("detailMessage", ex.getMessage())));
    }

    private ErrorResponseDto toErrorResponseDto(String errorMsg, Map<String, String> details) {
        return ErrorResponseDto.builder()
                .error(errorMsg)
                .details(details)
                .build();
    }
}
