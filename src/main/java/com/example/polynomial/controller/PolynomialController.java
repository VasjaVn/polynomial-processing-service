package com.example.polynomial.controller;

import com.example.polynomial.model.dto.PolynomialRequestDto;
import com.example.polynomial.model.dto.PolynomialResponseDto;
import com.example.polynomial.service.PolynomialService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/polynomials")
public class PolynomialController {

    private final PolynomialService polynomialService;

    @PostMapping("/evaluate")
    public PolynomialResponseDto evaluatePolynomial(@Valid @RequestBody PolynomialRequestDto polynomialRequestDto) {
        return polynomialService.evaluatePolynomial(polynomialRequestDto);
    }

    @GetMapping("/evaluate")
    public PolynomialResponseDto evaluatePolynomial(@Valid @RequestParam("polynomial") String polynomial,
                                                    @Valid @NotNull @RequestParam("value") Integer value) {
        return polynomialService.evaluatePolynomial(polynomial, value);
    }

    @DeleteMapping("/delete")
    public PolynomialResponseDto deletePolynomial(@RequestParam("polynomial") String polynomial) {
        return polynomialService.deletePolynomial(polynomial);
    }
}
