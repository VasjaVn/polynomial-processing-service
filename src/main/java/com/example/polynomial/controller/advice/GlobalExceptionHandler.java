package com.example.polynomial.controller.advice;

import com.example.polynomial.model.dto.response.ErrorResponseDto;
import io.lettuce.core.RedisException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> details.put(error.getField(), error.getDefaultMessage()));

        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .error("Request params are not valid.")
                .details(details)
                .build();

        return ResponseEntity.status(BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(RedisException.class)
    public ResponseEntity<ErrorResponseDto> handleRedisException(RedisException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .error("Redis: " + ex.getMessage())
                .details(Map.of("detailMessage", ex.getCause().getMessage()))
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponseDto> handleConnectException(ConnectException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .error("Can't connect to DataBase.")
                .details(Map.of("detailMessage", ex.getMessage()))
                .build();

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponseDto> handleSQLException(SQLException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .error("Error during communication with DataBase.")
                .details(Map.of("detailMessage", ex.getMessage().split("\\n")[0]))
                .build();

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }
}
