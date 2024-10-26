package com.example.polynomial.service;

import com.example.polynomial.model.dto.request.PolynomialRequestDto;
import com.example.polynomial.model.dto.response.PolynomialResponseDto;

public interface PolynomialService {

    PolynomialResponseDto evaluatePolynomial(PolynomialRequestDto polynomialRequestDto);

    PolynomialResponseDto evaluatePolynomial(String rawPolynomial, Integer value);

    PolynomialResponseDto deletePolynomial(String rawPolynomial);
}
