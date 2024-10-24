package com.example.polynomial.model.dto.response;

import com.example.polynomial.model.domain.Polynomial;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder(builderClassName = "Builder")
public record PolynomialResponseDto(String rawPolynomial,
                                    String simplifiedPolynomial,
                                    String multipliedPolynomial,
                                    @JsonInclude(NON_NULL)
                                    Polynomial.CalculationResult calculationResult) {}
