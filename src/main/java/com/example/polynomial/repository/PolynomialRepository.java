package com.example.polynomial.repository;

import com.example.polynomial.model.entity.PolynomialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PolynomialRepository extends JpaRepository<PolynomialEntity, Integer> {

//    @Query("SELECT p FROM PolynomialEntity p FETCH JOIN p.calculates")
//    PolynomialEntity findAndFetchOneResult(@Param("id") Integer id, @Param("value") Integer value);
}
