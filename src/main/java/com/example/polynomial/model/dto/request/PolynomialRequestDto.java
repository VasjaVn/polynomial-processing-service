package com.example.polynomial.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static com.example.polynomial.util.PolynomialRegEx.REGEX_SSMM;

public record PolynomialRequestDto(@NotBlank(message = "This field is empty.")
//                                   @Pattern(regexp = REGEX_POLYNOMIAL_IS_VALID)
                                   @Pattern(regexp = REGEX_SSMM, message = "This field has invalid polynomial.")
                                   String polynomial,
                                   @NotNull
                                   Integer value) {}
