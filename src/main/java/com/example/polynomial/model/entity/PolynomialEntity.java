package com.example.polynomial.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "polynomials")
@Builder(builderClassName = "Builder")
public class PolynomialEntity implements Serializable {

    @Id
    private Integer id;

    private String polynomial;

    @OneToMany(mappedBy = "polynomial", cascade = CascadeType.ALL)
    private List<PolynomialCalculateEntity> polynomialCalculates;
}

