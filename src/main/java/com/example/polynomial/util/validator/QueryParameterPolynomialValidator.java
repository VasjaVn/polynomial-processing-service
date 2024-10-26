package com.example.polynomial.util.validator;

import com.example.polynomial.util.validator.annotaion.ValidQueryParameterPolynomial;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.example.polynomial.util.PolynomialRegEx.REGEX_VALID_POLYNOMIAL;

public class QueryParameterPolynomialValidator implements ConstraintValidator<ValidQueryParameterPolynomial, String> {

    @Override
    public boolean isValid(String polynomial, ConstraintValidatorContext context) {
        return polynomial.matches(REGEX_VALID_POLYNOMIAL);
    }
}
