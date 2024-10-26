package com.example.polynomial.repository;

import com.example.polynomial.model.entity.PolynomialEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class PolynomialRepositoryIT extends RepositoryBaseIT {

    @Autowired
    private PolynomialRepository polynomialRepository;

    @Test
    public void findById_WhenPolynomialId_ThenReturnPolynomialEntity() {
        Optional<PolynomialEntity> optionalPolynomialEntity = polynomialRepository.findById(POLYNOMIAL_ID);

        assertNotNull(optionalPolynomialEntity);
        assertTrue(optionalPolynomialEntity.isPresent());

        PolynomialEntity polynomialEntity = optionalPolynomialEntity.get();
        assertEquals(POLYNOMIAL_ID, polynomialEntity.getId());
        assertEquals(POLYNOMIAL, polynomialEntity.getPolynomial());
    }

    @Test
    public void findById_WhenPolynomialIdIsAbsent_ThenReturnNull() {
        Optional<PolynomialEntity> optionalPolynomialEntity = polynomialRepository.findById(POLYNOMIAL_ID_IS_ABSENT);

        assertNotNull(optionalPolynomialEntity);
        assertTrue(optionalPolynomialEntity.isEmpty());
    }
}
