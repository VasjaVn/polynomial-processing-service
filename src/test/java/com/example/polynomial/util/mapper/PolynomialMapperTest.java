package com.example.polynomial.util.mapper;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.model.dto.request.PolynomialRequestDto;
import com.example.polynomial.model.dto.response.PolynomialResponseDto;
import com.example.polynomial.model.entity.PolynomialEntity;
import org.junit.jupiter.api.Test;

import static com.example.polynomial.model.domain.CalculationResultSource.EVALUATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PolynomialMapperTest {

    private static final String RAW_POLYNOMIAL = "x^2 + 4*x - 2 * x - 20 + 10";
    private static final String NORMALIZED_POLYNOMIAL = "x^2+4*x-2*x-20+10";
    private static final String SIMPLIFIED_POLYNOMIAL = "x^2+2*x-10";
    private static final int SIMPLIFIED_POLYNOMIAL_HASH = 2014176811;
    private static final double RESULT = -7.0;



    private static final String RAW_POLYNOMIAL_WITH_MULTIPLIERS = "(x^2 + 1)*(x^2 - 1)";
    private static final String NORMALIZED_POLYNOMIAL_WITH_MULTIPLIERS = "(x^2+1)*(x^2-1)";
    private static final String SIMPLIFIED_POLYNOMIAL_WITH_MULTIPLIERS = "x^4-1";
    private static final int SIMPLIFIED_POLYNOMIAL_WITH_MULTIPLIERS_HASH = 113674290;
    private static final double RESULT_WITH_MULTIPLIERS = 0.0;

    private static final int VALUE = 1;

    private final PolynomialMapper underTest = new PolynomialMapper();

    @Test
    public void toDomain_WhenPolynomialRequestDto_ThenReturnPolynomial() {
        PolynomialRequestDto polynomialRequestDto =
                new PolynomialRequestDto(RAW_POLYNOMIAL, VALUE);

        Polynomial polynomial = underTest.toDomain(polynomialRequestDto);

        assertEquals(RAW_POLYNOMIAL, polynomial.getRaw());
        assertEquals(NORMALIZED_POLYNOMIAL, polynomial.getNormalized());
        assertNull(polynomial.getSimplified());
        assertNull(polynomial.getMultiplied());
        assertNotNull(polynomial.getCalculationResult());
        assertEquals(VALUE, polynomial.getCalculationResult().getValue());
        assertNull(polynomial.getCalculationResult().getResult());
        assertNull(polynomial.getCalculationResult().getCalculationResultSource());
        assertNotNull(polynomial.getMultipliers());
        assertTrue(polynomial.getMultipliers().isEmpty());
        assertFalse(polynomial.hasMultipliers());
        assertNotNull(polynomial.getAnatomy());
        assertNull(polynomial.getAnatomy().getListTerms());
        assertNull(polynomial.getAnatomy().getMapDegreeCoeff());
    }

    @Test
    public void toDomain_WhenPolynomialRequestDtoWithMultipliers_ThenReturnPolynomial() {
        PolynomialRequestDto polynomialRequestDto =
                new PolynomialRequestDto(RAW_POLYNOMIAL_WITH_MULTIPLIERS, VALUE);

        Polynomial polynomial = underTest.toDomain(polynomialRequestDto);

        assertEquals(RAW_POLYNOMIAL_WITH_MULTIPLIERS, polynomial.getRaw());
        assertEquals(NORMALIZED_POLYNOMIAL_WITH_MULTIPLIERS, polynomial.getNormalized());
        assertNull(polynomial.getSimplified());
        assertNull(polynomial.getMultiplied());
        assertNotNull(polynomial.getCalculationResult());
        assertEquals(VALUE, polynomial.getCalculationResult().getValue());
        assertNull(polynomial.getCalculationResult().getResult());
        assertNull(polynomial.getCalculationResult().getCalculationResultSource());
        assertNotNull(polynomial.getMultipliers());
        assertFalse(polynomial.getMultipliers().isEmpty());
        assertTrue(polynomial.hasMultipliers());
        assertNotNull(polynomial.getAnatomy());
        assertNull(polynomial.getAnatomy().getListTerms());
        assertNull(polynomial.getAnatomy().getMapDegreeCoeff());
    }

    @Test
    public void toDomain_WhenRawPolynomial_ThenReturnPolynomial() {
        Polynomial polynomial = underTest.toDomain(RAW_POLYNOMIAL);

        assertEquals(RAW_POLYNOMIAL, polynomial.getRaw());
        assertEquals(NORMALIZED_POLYNOMIAL, polynomial.getNormalized());
        assertNull(polynomial.getSimplified());
        assertNull(polynomial.getMultiplied());
        assertNotNull(polynomial.getCalculationResult());
        assertNull(polynomial.getCalculationResult().getValue());
        assertNull(polynomial.getCalculationResult().getResult());
        assertNull(polynomial.getCalculationResult().getCalculationResultSource());
        assertNotNull(polynomial.getMultipliers());
        assertTrue(polynomial.getMultipliers().isEmpty());
        assertFalse(polynomial.hasMultipliers());
        assertNotNull(polynomial.getAnatomy());
        assertNull(polynomial.getAnatomy().getListTerms());
        assertNull(polynomial.getAnatomy().getMapDegreeCoeff());
    }

    @Test
    public void toDomain_WhenRawPolynomialWithMultipliers_ThenReturnPolynomial() {
        Polynomial polynomial =
                underTest.toDomain(RAW_POLYNOMIAL_WITH_MULTIPLIERS);

        assertEquals(RAW_POLYNOMIAL_WITH_MULTIPLIERS, polynomial.getRaw());
        assertEquals(NORMALIZED_POLYNOMIAL_WITH_MULTIPLIERS, polynomial.getNormalized());
        assertNull(polynomial.getSimplified());
        assertNull(polynomial.getMultiplied());
        assertNotNull(polynomial.getCalculationResult());
        assertNull(polynomial.getCalculationResult().getValue());
        assertNull(polynomial.getCalculationResult().getResult());
        assertNull(polynomial.getCalculationResult().getCalculationResultSource());
        assertNotNull(polynomial.getMultipliers());
        assertFalse(polynomial.getMultipliers().isEmpty());
        assertTrue(polynomial.hasMultipliers());
        assertNotNull(polynomial.getAnatomy());
        assertNull(polynomial.getAnatomy().getListTerms());
        assertNull(polynomial.getAnatomy().getMapDegreeCoeff());
    }

    @Test
    public void toEntity_WhenPolynomial_ThenReturnPolynomialEntity() {
        Polynomial.CalculationResult calculationResult = new Polynomial.CalculationResult();
        calculationResult.setValue(VALUE);
        calculationResult.setResult(RESULT);
        calculationResult.setCalculationResultSource(EVALUATION);

        Polynomial polynomial = new Polynomial(RAW_POLYNOMIAL);
        polynomial.setSimplified(SIMPLIFIED_POLYNOMIAL);
        polynomial.setCalculationResult(calculationResult);

        PolynomialEntity polynomialEntity = underTest.toEntity(polynomial);

        assertNotNull(polynomialEntity);
        assertEquals(SIMPLIFIED_POLYNOMIAL_HASH, polynomialEntity.getId());
        assertEquals(SIMPLIFIED_POLYNOMIAL, polynomialEntity.getPolynomial());
        assertNotNull(polynomialEntity.getPolynomialCalculates());
        assertEquals(1, polynomialEntity.getPolynomialCalculates().size());
        assertEquals(VALUE, polynomialEntity.getPolynomialCalculates().get(0).getValue());
        assertEquals(RESULT, polynomialEntity.getPolynomialCalculates().get(0).getResult());
    }

    @Test
    public void toEntity_WhenPolynomialWithMultipliers_ThenReturnPolynomialEntity() {
        Polynomial.CalculationResult calculationResult = new Polynomial.CalculationResult();
        calculationResult.setValue(VALUE);
        calculationResult.setResult(RESULT_WITH_MULTIPLIERS);
        calculationResult.setCalculationResultSource(EVALUATION);

        Polynomial polynomial = new Polynomial(RAW_POLYNOMIAL_WITH_MULTIPLIERS);
        polynomial.setSimplified(SIMPLIFIED_POLYNOMIAL_WITH_MULTIPLIERS);
        polynomial.setCalculationResult(calculationResult);

        PolynomialEntity polynomialEntity = underTest.toEntity(polynomial);

        assertNotNull(polynomialEntity);
        assertEquals(SIMPLIFIED_POLYNOMIAL_WITH_MULTIPLIERS_HASH, polynomialEntity.getId());
        assertEquals(SIMPLIFIED_POLYNOMIAL_WITH_MULTIPLIERS, polynomialEntity.getPolynomial());
        assertNotNull(polynomialEntity.getPolynomialCalculates());
        assertEquals(1, polynomialEntity.getPolynomialCalculates().size());
        assertEquals(VALUE, polynomialEntity.getPolynomialCalculates().get(0).getValue());
        assertEquals(RESULT_WITH_MULTIPLIERS, polynomialEntity.getPolynomialCalculates().get(0).getResult());
    }

    @Test
    public void toDto_WhenPolynomial_ThenReturnPolynomialResponseDto() {
        Polynomial.CalculationResult calculationResult = new Polynomial.CalculationResult();
        calculationResult.setValue(VALUE);
        calculationResult.setResult(RESULT);
        calculationResult.setCalculationResultSource(EVALUATION);

        Polynomial polynomial = new Polynomial(RAW_POLYNOMIAL);
        polynomial.setSimplified(SIMPLIFIED_POLYNOMIAL);
        polynomial.setMultiplied(null);
        polynomial.setCalculationResult(calculationResult);

        PolynomialResponseDto polynomialResponseDto = underTest.toDto(polynomial);

        assertNotNull(polynomialResponseDto);
        assertEquals(RAW_POLYNOMIAL, polynomialResponseDto.rawPolynomial());
        assertEquals(SIMPLIFIED_POLYNOMIAL, polynomialResponseDto.simplifiedPolynomial());
        assertNull(polynomialResponseDto.multipliedPolynomial());
        assertNotNull(polynomialResponseDto.calculationResult());
        assertEquals(VALUE, polynomialResponseDto.calculationResult().getValue());
        assertEquals(RESULT, polynomialResponseDto.calculationResult().getResult());
        assertEquals(EVALUATION, polynomialResponseDto.calculationResult().getCalculationResultSource());
    }

    @Test
    public void toDto_WhenPolynomialWithMultipliers_ThenReturnPolynomialResponseDto() {
        Polynomial.CalculationResult calculationResult = new Polynomial.CalculationResult();
        calculationResult.setValue(VALUE);
        calculationResult.setResult(RESULT_WITH_MULTIPLIERS);
        calculationResult.setCalculationResultSource(EVALUATION);

        Polynomial polynomial = new Polynomial(RAW_POLYNOMIAL_WITH_MULTIPLIERS);
        polynomial.setSimplified(null);
        polynomial.setMultiplied(SIMPLIFIED_POLYNOMIAL_WITH_MULTIPLIERS);
        polynomial.setCalculationResult(calculationResult);

        PolynomialResponseDto polynomialResponseDto = underTest.toDto(polynomial);

        assertNotNull(polynomialResponseDto);
        assertEquals(RAW_POLYNOMIAL_WITH_MULTIPLIERS, polynomialResponseDto.rawPolynomial());
        assertNull(polynomialResponseDto.simplifiedPolynomial());
        assertEquals(SIMPLIFIED_POLYNOMIAL_WITH_MULTIPLIERS, polynomialResponseDto.multipliedPolynomial());
        assertNotNull(polynomialResponseDto.calculationResult());
        assertEquals(VALUE, polynomialResponseDto.calculationResult().getValue());
        assertEquals(RESULT_WITH_MULTIPLIERS, polynomialResponseDto.calculationResult().getResult());
        assertEquals(EVALUATION, polynomialResponseDto.calculationResult().getCalculationResultSource());
    }

    @Test
    public void toDtoWithoutCalculationResult_WhenPolynomial_ThenReturnPolynomialResponseDto() {
        Polynomial polynomial = new Polynomial(RAW_POLYNOMIAL);
        polynomial.setSimplified(SIMPLIFIED_POLYNOMIAL);
        polynomial.setMultiplied(null);
        polynomial.setCalculationResult(null);

        PolynomialResponseDto polynomialResponseDto =
                underTest.toDtoWithoutCalculationResult(polynomial);

        assertNotNull(polynomialResponseDto);
        assertEquals(RAW_POLYNOMIAL, polynomialResponseDto.rawPolynomial());
        assertEquals(SIMPLIFIED_POLYNOMIAL, polynomialResponseDto.simplifiedPolynomial());
        assertNull(polynomialResponseDto.multipliedPolynomial());
        assertNull(polynomialResponseDto.calculationResult());
    }

    @Test
    public void toDtoWithoutCalculationResult_WhenPolynomialWithMultipliers_ThenReturnPolynomialResponseDto() {
        Polynomial polynomial = new Polynomial(RAW_POLYNOMIAL_WITH_MULTIPLIERS);
        polynomial.setSimplified(null);
        polynomial.setMultiplied(SIMPLIFIED_POLYNOMIAL_WITH_MULTIPLIERS);
        polynomial.setCalculationResult(null);

        PolynomialResponseDto polynomialResponseDto =
                underTest.toDtoWithoutCalculationResult(polynomial);

        assertNotNull(polynomialResponseDto);
        assertEquals(RAW_POLYNOMIAL_WITH_MULTIPLIERS, polynomialResponseDto.rawPolynomial());
        assertNull(polynomialResponseDto.simplifiedPolynomial());
        assertEquals(SIMPLIFIED_POLYNOMIAL_WITH_MULTIPLIERS, polynomialResponseDto.multipliedPolynomial());
        assertNull(polynomialResponseDto.calculationResult());
    }
}
