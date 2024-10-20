package com.example.polynomial.service;

import com.example.polynomial.model.dto.PolynomialRequestDto;
import com.example.polynomial.model.dto.PolynomialResponseDto;

public interface PolynomialService {
    PolynomialResponseDto evaluatePolynomial(PolynomialRequestDto polynomialRequestDto);

    PolynomialResponseDto evaluatePolynomial(String rawPolynomial, Integer value);

    PolynomialResponseDto deletePolynomial(String rawPolynomial);
}
