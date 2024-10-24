package com.example.polynomial.util.validator;

import com.example.polynomial.util.validator.annotaion.ValidQueryParameterValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class QueryParameterValueValidator implements ConstraintValidator<ValidQueryParameterValue, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null;
    }
}
