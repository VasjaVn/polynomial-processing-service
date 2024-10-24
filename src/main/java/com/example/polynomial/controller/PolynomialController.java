package com.example.polynomial.controller;

import com.example.polynomial.config.security.filter.JwtAuthenticationFilterErrorHandler;
import com.example.polynomial.model.dto.response.PolynomialResponseDto;
import com.example.polynomial.service.PolynomialService;
import com.example.polynomial.util.validator.annotaion.ValidQueryParameterPolynomial;
import com.example.polynomial.util.validator.annotaion.ValidQueryParameterValue;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/polynomials")
public class PolynomialController {

    private final PolynomialService polynomialService;

    private final JwtAuthenticationFilterErrorHandler jwtAuthenticationFilterErrorHandler;

//    @PostMapping("/evaluate")
//    public PolynomialResponseDto evaluatePolynomial(@Valid @RequestBody PolynomialRequestDto polynomialRequestDto) {
//        jwtAuthenticationFilterErrorHandler.handleError();
//
//        log.info("POST request: evaluate polynomial [ {} ]", polynomialRequestDto);
//        return polynomialService.evaluatePolynomial(polynomialRequestDto);
//    }

    @GetMapping("/evaluate")
    public PolynomialResponseDto evaluatePolynomial(@ValidQueryParameterPolynomial @RequestParam("polynomial") String polynomial,
                                                    @ValidQueryParameterValue @RequestParam("value") Integer value) {
        jwtAuthenticationFilterErrorHandler.handleError();

        log.info("GET request: evaluate polynomial - [ polynomial=\"{}\", value=\"{}\" ]", polynomial, value);
        return polynomialService.evaluatePolynomial(polynomial, value);
    }

    @DeleteMapping
    public PolynomialResponseDto deletePolynomial(@ValidQueryParameterPolynomial @RequestParam("polynomial") String polynomial) {
        jwtAuthenticationFilterErrorHandler.handleError();

        log.info("DELETE request: delete polynomial - [ polynomial=\"{}\" ]", polynomial);
        return polynomialService.deletePolynomial(polynomial);
    }
}
