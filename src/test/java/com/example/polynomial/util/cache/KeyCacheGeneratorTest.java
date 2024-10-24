package com.example.polynomial.util.cache;

import com.example.polynomial.model.domain.Polynomial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KeyCacheGeneratorTest {

    private final KeyCacheGenerator underTest = new KeyCacheGenerator();

    private Polynomial polynomial;

    @BeforeEach
    public void startUp() {
        Polynomial.CalculationResult calculationResult = new Polynomial.CalculationResult();
        calculationResult.setValue(1);
        calculationResult.setResult(-1.0);

        polynomial = new Polynomial("x^2+3*x-5");
        polynomial.setSimplified("x^2+3*x-5");
        polynomial.setCalculationResult(calculationResult);
    }

    @Test
    public void givenNullPolynomial_whenGenerateKeyCache_thenReturnKeyCache() {
        assertEquals("x^2+3*x-5|1", underTest.generateKeyFromPolynomial(polynomial));
    }

    @Test
    public void givenNullPolynomial_whenGenerateKeyCache_thenThrowsIllegalArgumentException() {
        polynomial = null;
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.generateKeyFromPolynomial(polynomial);
        });
    }

    @Test
    public void givenPolynomialHasSimplifiedIsNull_whenGenerateKeyCache_thenThrowsIllegalArgumentException() {
        polynomial.setSimplified(null);
        polynomial.setMultiplied(null);

        assertThrows(IllegalArgumentException.class, () -> {
            underTest.generateKeyFromPolynomial(polynomial);
        });
    }

    @Test
    public void givenPolynomialHasCalculationResultIsNull_whenGenerateKeyCache_thenThrowsIllegalArgumentException() {
        polynomial.setCalculationResult(null);

        assertThrows(IllegalArgumentException.class, () -> {
            underTest.generateKeyFromPolynomial(polynomial);
        });
    }

    @Test
    public void givenPolynomialHasValueOfCalculationResultIsNull_whenGenerateKeyCache_thenThrowsIllegalArgumentException() {
        Polynomial.CalculationResult calculationResult = polynomial.getCalculationResult();
        calculationResult.setValue(null);
        polynomial.setCalculationResult(calculationResult);

        assertThrows(IllegalArgumentException.class, () -> {
            underTest.generateKeyFromPolynomial(polynomial);
        });
    }
}
