package com.example.polynomial.util.validator.annotaion;

import com.example.polynomial.util.validator.QueryParameterPolynomialValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = QueryParameterPolynomialValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidQueryParameterPolynomial {
    String message() default "Polynomial is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
