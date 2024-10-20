package com.example.polynomial.util.validator;

import com.example.polynomial.exception.PolynomialHasOnlyOneMultiplier;

import java.util.List;

import static com.example.polynomial.util.PolynomialRegEx.REGEX_POLYNOMIAL_VALID_CHARACTERS;

public class PolynomialValidator {

    public static void checkNumberOfTerm() {

    }

    public static void checkVariableOfTerm() {

    }

    public static void checkPolynomialHasOnlyValidCharacters(String polynomial) {
        if (polynomial.matches(REGEX_POLYNOMIAL_VALID_CHARACTERS)) {
            System.out.println("checkPolynomialHasOnlyValidCharacters: TRUE");
        } else {
            System.out.println("checkPolynomialHasOnlyValidCharacters: FALSE");
        }
    }

    public static void checkPolynomialHasMoreThanOneMultipliers(List<String> multipliers) {
        if (multipliers != null && multipliers.size() == 1) {
            throw new PolynomialHasOnlyOneMultiplier("");
        }
    }
}
