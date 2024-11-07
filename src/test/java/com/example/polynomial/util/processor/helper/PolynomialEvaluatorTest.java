package com.example.polynomial.util.processor.helper;

import com.example.polynomial.model.domain.Polynomial;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PolynomialEvaluatorTest {

    private final PolynomialEvaluator underTest = new PolynomialEvaluator();

    @Test
    public void givenPolynomial_whenEvaluate_thenEvaluatedResultWritePolynomial() {
        Polynomial polynomial = createPolynomial("2*x^3+5*x-20+10", "2*x^3+5*x-10",
                Map.of(0, -10, 1, 5, 3, 2), 3);

        underTest.evaluate(polynomial);
        assertEquals(59.0, polynomial.getCalculationResult().getResult());
    }

    @Test
    public void givenConstantPolynomial_whenEvaluate_thenWriteConstantToPolynomialAsResult() {
        Polynomial polynomial = createPolynomial("10", "10", Map.of(0, 10), 3);

        underTest.evaluate(polynomial);
        assertEquals(10.0, polynomial.getCalculationResult().getResult());
    }

    @Test
    public void givenNullPolynomial_whenEvaluate_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.evaluate(null);
        });
    }

    @Test
    public void givenPolynomialWithAnatomyIsEqualNull_whenEvaluate_thenThrowsIllegalArgumentException() {
        Polynomial polynomial = new Polynomial("");
        polynomial.setAnatomy(null);

        assertThrows(IllegalArgumentException.class, () -> {
            underTest.evaluate(polynomial);
        });
    }

    @Test
    public void givenPolynomialWithMapDegreeTermsIsEqualNull_whenEvaluate_thenThrowsIllegalArgumentException() {
        Polynomial polynomial = createPolynomial("", "", null, 3);

        assertThrows(IllegalArgumentException.class, () -> {
            underTest.evaluate(polynomial);
        });
    }

    @Test
    public void givenPolynomialWithMapDegreeTermsIsEmpty_whenEvaluate_thenThrowsIllegalArgumentException() {
        Polynomial polynomial = createPolynomial("", "", Map.of(), 3);

        assertThrows(IllegalArgumentException.class, () -> {
            underTest.evaluate(polynomial);
        });
    }

    private Polynomial createPolynomial(String strPolynomial,
                                        String simplifiedPolynomial,
                                        Map<Integer, Integer> mapDegreeTerms,
                                        Integer value) {
        Polynomial.CalculationResult calculationResult = new Polynomial.CalculationResult();
        calculationResult.setValue(value);

        Polynomial.Anatomy anatomy = new Polynomial.Anatomy();
        anatomy.setMapDegreeCoeff(mapDegreeTerms);

        Polynomial polynomial = new Polynomial(strPolynomial);
        polynomial.setSimplified(simplifiedPolynomial);
        polynomial.setCalculationResult(calculationResult);
        polynomial.setAnatomy(anatomy);

        return polynomial;
    }
}
