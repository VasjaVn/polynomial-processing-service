package com.example.polynomial.repository;

import com.example.polynomial.model.entity.PolynomialCalculateEntity;
import com.example.polynomial.model.entity.PolynomialEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class PolynomialCalculateRepositoryIT extends RepositoryBaseIT {

    @Autowired
    private PolynomialCalculateRepository polynomialCalculateRepository;

    @Test
    public void findOneByPolynomialIdAndValue_WhenPolynomialIdAndValue_ThenReturnPolynomialCalculateEntity() {
        PolynomialCalculateEntity polynomialCalculateEntity =
                polynomialCalculateRepository.findOneByPolynomialIdAndValue(POLYNOMIAL_ID, VALUE);

        assertNotNull(polynomialCalculateEntity);
        assertEquals(POLYNOMIAL_CALCULATE_ID, polynomialCalculateEntity.getId());
        assertEquals(VALUE, polynomialCalculateEntity.getValue());
        assertEquals(RESULT, polynomialCalculateEntity.getResult());

        PolynomialEntity polynomialEntity = polynomialCalculateEntity.getPolynomial();
        assertNotNull(polynomialEntity);
        assertEquals(POLYNOMIAL_ID, polynomialEntity.getId());
        assertEquals(POLYNOMIAL, polynomialEntity.getPolynomial());
    }

    @Test
    public void findOneByPolynomialIdAndValue_WhenPolynomialIdIsPresentAndValueIsAbsent_ThenReturnNull() {
        PolynomialCalculateEntity polynomialCalculateEntity =
                polynomialCalculateRepository.findOneByPolynomialIdAndValue(POLYNOMIAL_ID, VALUE_IS_ABSENT);

        assertNull(polynomialCalculateEntity);
    }

    @Test
    public void findOneByPolynomialIdAndValue_WhenPolynomialIdIsAbsentAndValueIsPresent_ThenReturnNull() {
        PolynomialCalculateEntity polynomialCalculateEntity = polynomialCalculateRepository
                .findOneByPolynomialIdAndValue(POLYNOMIAL_CALCULATE_ID_IS_ABSENT, VALUE);

        assertNull(polynomialCalculateEntity);
    }

    @Test
    public void findOneByPolynomialIdAndValue_WhenPolynomialIdIsAbsentAndValueIsAbsent_ThenReturnNull() {
        PolynomialCalculateEntity polynomialCalculateEntity = polynomialCalculateRepository
                .findOneByPolynomialIdAndValue(POLYNOMIAL_CALCULATE_ID_IS_ABSENT, VALUE_IS_ABSENT);

        assertNull(polynomialCalculateEntity);
    }
}
