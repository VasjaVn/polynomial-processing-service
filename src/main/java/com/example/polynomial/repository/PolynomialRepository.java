package com.example.polynomial.repository;

import com.example.polynomial.model.entity.PolynomialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolynomialRepository extends JpaRepository<PolynomialEntity, Integer> {
}
