package com.example.polynomial.util;

import com.example.polynomial.util.validator.PolynomialValidator;
import org.junit.jupiter.api.Test;

public class PolynomialValidatorTest {

    private final PolynomialValidator polynomialValidator = new PolynomialValidator();

    @Test
    public void test() {
//        polynomialValidator.checkPolynomialHasOnlyValidCharacters("2*x^2+4*x-3");
        polynomialValidator.checkPolynomialHasOnlyValidCharacters("23");
    }

    @Test
    public void test2() {
        String polynomial = "-2 *x^2   +X-  5";
        if (polynomial.matches("^[\\dxX*^\\-+\\s]+$")) {
            System.out.println("checkPolynomialHasOnlyValidCharacters: TRUE");
        } else {
            System.out.println("checkPolynomialHasOnlyValidCharacters: FALSE");
        }
    }

    @Test
    public void test3() {
        String polynomial = "(x+2)*(x-10)";
        if (polynomial.matches("^[\\dxX*^\\-+()]+$")) {
            System.out.println("checkPolynomialHasOnlyValidCharacters: TRUE");
        } else {
            System.out.println("checkPolynomialHasOnlyValidCharacters: FALSE");
        }
    }
}
