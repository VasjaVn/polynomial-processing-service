package com.example.polynomial.util.processor.helper;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.converter.PolynomialConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PolynomialMultiplierTest {

    @InjectMocks
    private PolynomialMultiplier underTest;

    @Mock
    private PolynomialConverter polynomialConverterMock;


    @Test
    public void givenPolynomial_whenMultiply_thenMultipliedResultWriteToPolynomialAndEvaluateAnatomyOfPolynomial() {
        Polynomial polynomial = new Polynomial("(x-1)*(2*x-1)");

        when(polynomialConverterMock.toListTerms("x-1"))
                .thenReturn(List.of("x", "-1"));
        when(polynomialConverterMock.toMapDegreeTerms(List.of("x", "-1")))
                .thenReturn(Map.of(0, -1, 1, 1));

        when(polynomialConverterMock.toListTerms("2*x-1"))
                .thenReturn(List.of("2*x", "-1"));
        when(polynomialConverterMock.toMapDegreeTerms(List.of("2*x", "-1")))
                .thenReturn(Map.of(0, -1, 1, 2));

        when(polynomialConverterMock.toPolynomialAsString(List.of(1, -3, 2)))
                .thenReturn("2*x^2-3*x+1");

        when(polynomialConverterMock.toListTerms("2*x^2-3*x+1"))
                .thenReturn(List.of("2*x^3", "-3*x", "1"));
        when(polynomialConverterMock.toMapDegreeTerms(List.of("2*x^3", "-3*x", "1")))
                .thenReturn(Map.of(0, 1, 1, -3, 3, 2));

        underTest.multiply(polynomial);
        assertEquals("2*x^2-3*x+1", polynomial.getMultiplied());
        assertEquals(List.of("2*x^3", "-3*x", "1"), polynomial.getAnatomy().getListTerms());
        assertEquals(Map.of(0, 1, 1, -3, 3, 2), polynomial.getAnatomy().getMapDegreeTerms());
    }


    @Test
    public void givenNullPolynomial_whenMultiply_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.multiply(null);
        });
    }

    @Test
    public void givenPolynomialWithNullAnatomy_whenMultiply_thenThrowsIllegalArgumentException() {
        Polynomial polynomial = new Polynomial("");
        polynomial.setAnatomy(null);

        assertThrows(IllegalArgumentException.class, () -> {
            underTest.multiply(polynomial);
        });
    }
}
