package com.example.polynomial.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder")
@Table(name = "polynomial_calculates",
       indexes = @Index(name = "unique_index_polynomialId_value", columnList = "polynomial_id, value", unique = true))
public class PolynomialCalculateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer value;

    @Column(nullable = false)
    private Double result;

    @ManyToOne
    @JoinColumn(name = "polynomial_id", nullable = false)
    private PolynomialEntity polynomial;
}
