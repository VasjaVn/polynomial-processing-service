package com.example.polynomial.util.validator.annotaion;

import com.example.polynomial.util.validator.QueryParameterValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = QueryParameterValueValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidQueryParameterValue {
    String message() default "Value is NULL";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
