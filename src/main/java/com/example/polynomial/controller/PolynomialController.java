package com.example.polynomial.controller;

import com.example.polynomial.model.dto.request.PolynomialRequestDto;
import com.example.polynomial.model.dto.response.PolynomialResponseDto;
import com.example.polynomial.service.PolynomialService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/polynomials")
public class PolynomialController {

    private final PolynomialService polynomialService;

    @PostMapping("/evaluate")
    public PolynomialResponseDto evaluatePolynomial(@Valid @RequestBody PolynomialRequestDto polynomialRequestDto) {
        log.info("POST request: evaluate polynomial [{}]", polynomialRequestDto);
        return polynomialService.evaluatePolynomial(polynomialRequestDto);
    }

    @GetMapping("/evaluate")
    public PolynomialResponseDto evaluatePolynomial(@Valid @RequestParam("polynomial") String polynomial,
                                                    @Valid @NotNull @RequestParam("value") Integer value) {
        log.info("GET request: evaluate polynomial - [polynomial=\"{}\", value=\"{}\"]", polynomial, value);
        return polynomialService.evaluatePolynomial(polynomial, value);
    }

    @DeleteMapping("/delete")
    public PolynomialResponseDto deletePolynomial(@Valid @RequestParam("polynomial") String polynomial) {
        log.info("DELETE request: delete polynomial - [polynomial=\"{}\"]", polynomial);
        return polynomialService.deletePolynomial(polynomial);
    }
}
