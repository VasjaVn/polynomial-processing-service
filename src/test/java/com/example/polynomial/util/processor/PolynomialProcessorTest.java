package com.example.polynomial.util.processor;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.helper.PolynomialEvaluator;
import com.example.polynomial.util.processor.helper.PolynomialMultiplier;
import com.example.polynomial.util.processor.helper.PolynomialSimplifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PolynomialProcessorTest {

    @InjectMocks
    private PolynomialProcessor underTest;

    @Mock
    private PolynomialSimplifier polynomialSimplifierMock;

    @Mock
    private PolynomialMultiplier polynomialMultiplierMock;

    @Mock
    private PolynomialEvaluator polynomialEvaluatorMock;

    @BeforeEach
    public void setUp() {
//        Polynomial polynomial = new Polynomial("x")
    }

    @Test
    public void givenPolynomial_whenSimplify_thenUsePolynomialSimplifier() {
        Polynomial polynomial = new Polynomial("x^2-2*x+1");
        polynomial.setSimplified("x^2-2*x+1");

        underTest.simplifyPolynomial(polynomial);

        verify(polynomialSimplifierMock).simplify(polynomial);
        verify(polynomialMultiplierMock, times(0)).multiply(polynomial);
        verify(polynomialEvaluatorMock, times(0)).evaluate(polynomial);
    }

    @Test
    public void givenManyPolynomials_whenMultiply_thenUsePolynomialMultiplier() {
        Polynomial polynomial = new Polynomial("(x+1)(x-1)");
        polynomial.setMultiplied("x^2-1");

        underTest.multiplyPolynomials(polynomial);

        verify(polynomialMultiplierMock).multiply(polynomial);
        verify(polynomialSimplifierMock, times(0)).simplify(polynomial);
        verify(polynomialEvaluatorMock, times(0)).evaluate(polynomial);
    }

    @Test
    public void givenOnePolynomial_whenMultiply_thenThrowsIllegalArgumentException() {
        Polynomial polynomial = new Polynomial("x^2-2*x+1");

        assertThrows(IllegalArgumentException.class, () -> {
            underTest.multiplyPolynomials(polynomial);
        });
    }

    @Test
    public void givenPolynomial_whenEvaluate_thenUsePolynomialEvaluator() {
        Polynomial.CalculationResult calculationResult = new Polynomial.CalculationResult();
        calculationResult.setValue(1);
        calculationResult.setResult(2.0);

        Polynomial polynomial = new Polynomial("x^2+2*x-1");
        polynomial.setSimplified("x^2+2*x-1");
        polynomial.setCalculationResult(calculationResult);

        underTest.evaluatePolynomial(polynomial);

        verify(polynomialEvaluatorMock).evaluate(polynomial);
        verify(polynomialSimplifierMock, times(0)).simplify(polynomial);
        verify(polynomialMultiplierMock, times(0)).multiply(polynomial);
    }
}
