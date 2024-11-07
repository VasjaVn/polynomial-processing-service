package com.example.polynomial.util.processor.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PolynomialConverterTest {

    private final PolynomialConverter underTest = new PolynomialConverter();

    @Test
    public void givenListCoefficients_whenConvert_thenReturnPolynomial() {
        Assertions.assertEquals("5*x^4+9", underTest.toPolynomialAsString(List.of(9, 0, 0, 0, 5)));
        Assertions.assertEquals("6*x^3+5*x^2+2", underTest.toPolynomialAsString(List.of(2, 0, 5, 6)));
        Assertions.assertEquals("3*x", underTest.toPolynomialAsString(List.of(0, 3)));
        Assertions.assertEquals("5", underTest.toPolynomialAsString(List.of(5)));
    }

    @Test
    public void givenListCoefficientsIsNull_whenConvert_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.toPolynomialAsString(null);
        });
    }

    @Test
    public void givenEmptyListOfCoefficients_whenConvert_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.toPolynomialAsString(List.of());
        });
    }

    @Test
    public void givenListOfZeroCoefficients_whenConvert_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.toPolynomialAsString(List.of(0, 0, 0, 0));
        });
    }

    @Test
    public void givenPolynomial_whenConvert_thenReturnListTerms() {
        assertEquals(List.of("2*x^2", "-4*x", "9"), underTest.toListTerms("2*x^2-4*x+9"));
        assertEquals(List.of("2*x^2"), underTest.toListTerms("2*x^2"));
        assertEquals(List.of("2"), underTest.toListTerms("2"));
    }

    @Test
    public void givenPolynomialAsNull_whenConvert_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.toListTerms(null);
        });
    }

    @Test
    public void givenPolynomialAsEmptyString_whenConvert_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.toListTerms("");
        });
    }

    @Test
    public void givenListOfTerms_whenConvert_thenReturnMapOfDegreeTerms() {
        assertEquals(Map.of(0,45, 1, 5, 2, -4),
                underTest.toMapDegreeCoeff(List.of("-4*x^2", "5*x", "45")));
        assertEquals(Map.of(0,45), underTest.toMapDegreeCoeff(List.of("45")));
        assertEquals(Map.of(1,1), underTest.toMapDegreeCoeff(List.of("x")));
        assertEquals(Map.of(1,2), underTest.toMapDegreeCoeff(List.of("2*x")));
        assertEquals(Map.of(6,5), underTest.toMapDegreeCoeff(List.of("5*x^6")));
    }

    @Test
    public void givenListOfTermsIsEqualNll_whenConvert_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.toMapDegreeCoeff(null);
        });
    }

    @Test
    public void givenListOfTermsIsEmpty_whenConvert_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.toMapDegreeCoeff(List.of());
        });
    }
}
