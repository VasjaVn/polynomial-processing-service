package com.example.polynomial.model.dto;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.model.enums.DeletePolynomialStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder(builderClassName = "Builder")
public record PolynomialResponseDto(String rawPolynomial,
                                    String simplifiedPolynomial,
                                    String multipliedPolynomial,
                                    @JsonInclude(NON_NULL)
                                    Polynomial.CalculationResult calculationResult,
                                    @JsonInclude(NON_NULL)
                                    DeletePolynomialStatus deleteStatus) {}
