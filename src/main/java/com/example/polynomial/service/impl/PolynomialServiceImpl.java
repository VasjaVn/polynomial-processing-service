package com.example.polynomial.service.impl;

import com.example.polynomial.config.cache.CacheProperties;
import com.example.polynomial.model.entity.PolynomialCalculateEntity;
import com.example.polynomial.model.enums.DeletePolynomialStatus;
import com.example.polynomial.repository.PolynomialCalculateRepository;
import com.example.polynomial.util.mapper.PolynomialMapper;
import com.example.polynomial.model.dto.PolynomialRequestDto;
import com.example.polynomial.model.dto.PolynomialResponseDto;
import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.repository.PolynomialRepository;
import com.example.polynomial.service.PolynomialService;
import com.example.polynomial.util.processor.PolynomialProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.polynomial.model.enums.CalculationResultSource.CACHE;
import static com.example.polynomial.model.enums.CalculationResultSource.DATABASE;
import static com.example.polynomial.model.enums.CalculationResultSource.EVALUATION;
import static com.example.polynomial.model.enums.DeletePolynomialStatus.DELETED;
import static com.example.polynomial.model.enums.DeletePolynomialStatus.POLYNOMIAL_NOT_EXIST;
import static com.example.polynomial.util.validator.PolynomialValidator.checkPolynomialHasOnlyValidCharacters;
import static java.lang.Boolean.TRUE;

@Log
@Service
@RequiredArgsConstructor
public class PolynomialServiceImpl implements PolynomialService {

    private final CacheProperties cacheProperties;

    private final PolynomialRepository polynomialRepository;

    private final PolynomialCalculateRepository polynomialCalculateRepository;

//    private final RedisTemplate<String, Double> redisTemplate;

    private final PolynomialProcessor polynomialProcessor;

    private final PolynomialMapper polynomialMapper;

    @Transactional
    @Override
    public PolynomialResponseDto evaluatePolynomial(PolynomialRequestDto polynomialRequestDto) {
        checkPolynomialHasOnlyValidCharacters(polynomialRequestDto.polynomial());

        Polynomial polynomial = polynomialMapper.toDomain(polynomialRequestDto);
        doEvaluatePolynomial(polynomial);

        return polynomialMapper.toDto(polynomial);
    }

    @Transactional
    @Override
    public PolynomialResponseDto evaluatePolynomial(String rawPolynomial, Integer value) {
        Polynomial polynomial = polynomialMapper.toDomain(rawPolynomial);
        polynomial.getCalculationResult().setValue(value);
        doEvaluatePolynomial(polynomial);

        return polynomialMapper.toDto(polynomial);
    }

    @Override
    public PolynomialResponseDto deletePolynomial(String rawPolynomial) {
        DeletePolynomialStatus deletePolynomialStatus = DELETED;

        Polynomial polynomial = polynomialMapper.toDomain(rawPolynomial);
        doSimplifyOrMultiplyPolynomial(polynomial);

        Integer polynomialId = polynomial.getSimplifiedPolynomialHash();
        if (polynomialRepository.findById(polynomialId).isPresent()) {
            polynomialRepository.deleteById(polynomialId);
            log.info("Polynomial \"" + rawPolynomial + "\" was deleted.");
        } else {
            deletePolynomialStatus = POLYNOMIAL_NOT_EXIST;
            log.info("Polynomial \"" + rawPolynomial + "\" is not exist.");
        }

        return polynomialMapper.toDto(polynomial, deletePolynomialStatus);
    }

    private void doSimplifyOrMultiplyPolynomial(Polynomial polynomial) {
        if (polynomial.hasMultipliers()) {
            polynomialProcessor.multiplyPolynomials(polynomial);
        } else {
            polynomialProcessor.simplifyPolynomial(polynomial);
        }
    }

    private void doEvaluatePolynomial(Polynomial polynomial) {
        doSimplifyOrMultiplyPolynomial(polynomial);

//        String cacheKey = polynomial.getSimplifiedPolynomial() + "|" + polynomial.getCalculationResult().getValue();
//
//        if (TRUE.equals(redisTemplate.hasKey(cacheKey))) {
//            Double cacheResult = redisTemplate.opsForValue().get(cacheKey);
//
//            polynomial.getCalculationResult().setResult(cacheResult);
//            polynomial.getCalculationResult().setCalculationResultSource(CACHE);
//
//        } else {
            PolynomialCalculateEntity polynomialCalculateEntity =
                    polynomialCalculateRepository.findOneByPolynomialIdAndValue(
                            polynomial.getSimplifiedPolynomialHash(),
                            polynomial.getCalculationResult().getValue());

            if (polynomialCalculateEntity != null) {
                polynomial.getCalculationResult().setResult(polynomialCalculateEntity.getResult());
                polynomial.getCalculationResult().setCalculationResultSource(DATABASE);

            } else {
                polynomialProcessor.evaluatePolynomial(polynomial);
                polynomial.getCalculationResult().setCalculationResultSource(EVALUATION);

                polynomialRepository.save(
                        polynomialMapper.toEntity(polynomial));
            }

//            redisTemplate.opsForValue().set(cacheKey, polynomial.getCalculationResult().getResult(),
//                    cacheProperties.getTtl(), cacheProperties.getTimeUnit());
//        }
    }

//    private void doEvaluatePolynomial(Polynomial polynomial) {
//        doSimplifyOrMultiplyPolynomial(polynomial);
//
//        String cacheKey = polynomial.getSimplifiedPolynomial() + "|" + polynomial.getCalculationResult().getValue();
//
//        if (TRUE.equals(redisTemplate.hasKey(cacheKey))) {
//            Double cacheResult = redisTemplate.opsForValue().get(cacheKey);
//
//            polynomial.getCalculationResult().setResult(cacheResult);
//            polynomial.getCalculationResult().setCalculationResultSource(CACHE);
//
//        } else {
//            PolynomialCalculateEntity polynomialCalculateEntity =
//                    polynomialCalculateRepository.findOneByPolynomialIdAndValue(
//                            polynomial.getSimplifiedPolynomialHash(),
//                            polynomial.getCalculationResult().getValue());
//
//            if (polynomialCalculateEntity != null) {
//                polynomial.getCalculationResult().setResult(polynomialCalculateEntity.getResult());
//                polynomial.getCalculationResult().setCalculationResultSource(DATABASE);
//
//            } else {
//                polynomialProcessor.evaluatePolynomial(polynomial);
//                polynomial.getCalculationResult().setCalculationResultSource(EVALUATION);
//
//                polynomialRepository.save(
//                        polynomialMapper.toEntity(polynomial));
//            }
//
//            redisTemplate.opsForValue().set(cacheKey, polynomial.getCalculationResult().getResult(),
//                    cacheProperties.getTtl(), cacheProperties.getTimeUnit());
//        }
//    }
}
