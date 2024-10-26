package com.example.polynomial.repository;

import com.example.polynomial.model.entity.PolynomialCalculateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PolynomialCalculateRepository extends JpaRepository<PolynomialCalculateEntity, Long> {

    @Query("SELECT pc FROM PolynomialCalculateEntity pc WHERE pc.polynomial.id = :polynomialId AND pc.value = :value")
    PolynomialCalculateEntity findOneByPolynomialIdAndValue(@Param("polynomialId") Integer polynomialId,
                                                            @Param("value") Integer value);
}
