package com.example.polynomial.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static com.example.polynomial.util.PolynomialRegEx.REGEX_VALID_POLYNOMIAL;

public record PolynomialRequestDto(@NotBlank(message = "Polynomial is empty")
                                   @Pattern(regexp = REGEX_VALID_POLYNOMIAL, message = "Polynomial is not valid")
                                   String polynomial,
                                   @NotNull(message = "Value is absent")
                                   Integer value) {}
