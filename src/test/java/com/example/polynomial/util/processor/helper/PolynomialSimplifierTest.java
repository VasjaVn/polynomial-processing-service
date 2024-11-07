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
public class PolynomialSimplifierTest {

    @InjectMocks
    private PolynomialSimplifier underTest;

    @Mock
    private PolynomialConverter polynomialConverterMock;

    @Test
    public void givenPolynomial_whenSimplify_thenEvaluateAnatomyOfPolynomialAndWriteSimplifiedResultToPolynomial() {
        Polynomial polynomial = new Polynomial("2*x^3+5*x-10+3*x+4");

        when(polynomialConverterMock.toListTerms("2*x^3+5*x-10+3*x+4"))
                .thenReturn(List.of("2*x^3", "5*x", "-10", "3*x", "4"));
        when(polynomialConverterMock.toMapDegreeCoeff(List.of("2*x^3", "5*x", "-10", "3*x", "4")))
                .thenReturn(Map.of(0, -6, 1, 8, 3, 2));

        underTest.simplify(polynomial);

        assertEquals("2*x^3+8*x-6", polynomial.getSimplified());
        assertEquals(List.of("2*x^3", "5*x", "-10", "3*x", "4"), polynomial.getAnatomy().getListTerms());
        assertEquals(Map.of(0, -6, 1, 8, 3, 2), polynomial.getAnatomy().getMapDegreeCoeff());
    }

    @Test
    public void givenNullPolynomial_whenSimplify_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.simplify(null);
        });
    }

    @Test
    public void givenPolynomialWithNullAnatomy_whenSimplify_thenThrowsIllegalArgumentException() {
        Polynomial polynomial = new Polynomial("");
        polynomial.setAnatomy(null);

        assertThrows(IllegalArgumentException.class, () -> {
            underTest.simplify(polynomial);
        });
    }
}
