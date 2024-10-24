package com.example.polynomial.util.mapper;

import com.example.polynomial.model.dto.request.PolynomialRequestDto;
import com.example.polynomial.model.dto.response.PolynomialResponseDto;
import com.example.polynomial.model.entity.PolynomialCalculateEntity;
import com.example.polynomial.model.entity.PolynomialEntity;
import com.example.polynomial.model.domain.Polynomial;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PolynomialMapper {

    public Polynomial toDomain(PolynomialRequestDto dto) {
        Polynomial polynomial = Polynomial.builder()
                .raw(dto.polynomial())
                .build();

        polynomial.getCalculationResult().setValue(dto.value());

        return polynomial;
    }

    public Polynomial toDomain(String rawPolynomial) {
        return Polynomial.builder()
                .raw(rawPolynomial)
                .build();
    }

    public PolynomialEntity toEntity(Polynomial domain) {
        PolynomialCalculateEntity polynomialCalculateEntity = toPolynomialCalculateEntity(domain);

        PolynomialEntity polynomialEntity = PolynomialEntity.builder()
                .id(domain.getSimplifiedPolynomialHash())
                .polynomial(domain.getSimplifiedPolynomial())
                .polynomialCalculates(List.of(polynomialCalculateEntity))
                .build();

        polynomialCalculateEntity.setPolynomial(polynomialEntity);

        return polynomialEntity;
    }

    public PolynomialResponseDto toDto(Polynomial domain) {
        Polynomial.CalculationResult calculationResult = new Polynomial.CalculationResult();
        calculationResult.setValue(domain.getCalculationResult().getValue());
        calculationResult.setResult(domain.getCalculationResult().getResult());
        calculationResult.setCalculationResultSource(
                domain.getCalculationResult().getCalculationResultSource());

        return PolynomialResponseDto.builder()
                .rawPolynomial(domain.getRaw())
                .simplifiedPolynomial(domain.getSimplified())
                .multipliedPolynomial(domain.getMultiplied())
                .calculationResult(calculationResult)
                .build();
    }

    public PolynomialResponseDto toDtoWithoutCalculationResult(Polynomial domain) {
        return PolynomialResponseDto.builder()
                .rawPolynomial(domain.getRaw())
                .simplifiedPolynomial(domain.getSimplified())
                .multipliedPolynomial(domain.getMultiplied())
                .calculationResult(null)
                .build();
    }

    private PolynomialCalculateEntity toPolynomialCalculateEntity(Polynomial domain) {
        return PolynomialCalculateEntity.builder()
                .value(domain.getCalculationResult().getValue())
                .result(domain.getCalculationResult().getResult())
                .build();
    }
}
