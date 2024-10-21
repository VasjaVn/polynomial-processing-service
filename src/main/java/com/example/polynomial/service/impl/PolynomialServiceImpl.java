package com.example.polynomial.service.impl;

import com.example.polynomial.config.cache.CacheProperties;
import com.example.polynomial.model.entity.PolynomialCalculateEntity;
import com.example.polynomial.model.dto.response.enums.DeletePolynomialStatus;
import com.example.polynomial.repository.PolynomialCalculateRepository;
import com.example.polynomial.util.mapper.PolynomialMapper;
import com.example.polynomial.model.dto.request.PolynomialRequestDto;
import com.example.polynomial.model.dto.response.PolynomialResponseDto;
import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.repository.PolynomialRepository;
import com.example.polynomial.service.PolynomialService;
import com.example.polynomial.util.processor.PolynomialProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.polynomial.model.dto.response.enums.CalculationResultSource.CACHE;
import static com.example.polynomial.model.dto.response.enums.CalculationResultSource.DATABASE;
import static com.example.polynomial.model.dto.response.enums.CalculationResultSource.EVALUATION;
import static com.example.polynomial.model.dto.response.enums.DeletePolynomialStatus.DELETED;
import static com.example.polynomial.model.dto.response.enums.DeletePolynomialStatus.POLYNOMIAL_NOT_EXIST;
import static com.example.polynomial.util.Utils.generateKeyForCacheFromPolynomial;
import static java.lang.Boolean.TRUE;

@Log4j2
@Service
@RequiredArgsConstructor
public class PolynomialServiceImpl implements PolynomialService {

    private final CacheProperties cacheProperties;

    private final PolynomialRepository polynomialRepository;

    private final PolynomialCalculateRepository polynomialCalculateRepository;

    private final RedisTemplate<String, Double> redisTemplate;

    private final PolynomialProcessor polynomialProcessor;

    private final PolynomialMapper polynomialMapper;

    @Transactional
    @Override
    public PolynomialResponseDto evaluatePolynomial(PolynomialRequestDto polynomialRequestDto) {
//        checkPolynomialHasOnlyValidCharacters(polynomialRequestDto.polynomial());
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
            log.info("Polynomial \"{}\" was deleted.", rawPolynomial);

        } else {
            deletePolynomialStatus = POLYNOMIAL_NOT_EXIST;
            log.info("Polynomial \"{}\" is not exist.", rawPolynomial);
        }

        return polynomialMapper.toDto(polynomial, deletePolynomialStatus);
    }

    private void doSimplifyOrMultiplyPolynomial(Polynomial polynomial) {
        if (polynomial.hasMultipliers()) {
            log.info("Polynomial has multipliers: [polynomial=\"{}\"]", polynomial.getNormalized());
            polynomialProcessor.multiplyPolynomials(polynomial);

        } else {
            log.info("Polynomial is simple: [polynomial=\"{}\"]", polynomial.getNormalized());
            polynomialProcessor.simplifyPolynomial(polynomial);
        }
    }

    private void doEvaluatePolynomial(Polynomial polynomial) {
        doSimplifyOrMultiplyPolynomial(polynomial);

        String cacheKey = generateKeyForCacheFromPolynomial(polynomial);

        log.info("Generate cache key: cacheKey=\"{}\". [polynomial=\"{}\", value=\"{}\"] ",
                cacheKey, polynomial.getSimplifiedPolynomial(), polynomial.getCalculationResult().getValue());

        if (TRUE.equals(redisTemplate.hasKey(cacheKey))) {
            Double cacheResult = redisTemplate.opsForValue().get(cacheKey);

            polynomial.getCalculationResult().setResult(cacheResult);
            polynomial.getCalculationResult().setCalculationResultSource(CACHE);

            log.info("Cache has cached result \"{}\" for key=\"{}\". [polynomial=\"{}\"]",
                    cacheResult, cacheKey, polynomial.getSimplifiedPolynomial());

        } else {
            PolynomialCalculateEntity polynomialCalculateEntity =
                    polynomialCalculateRepository.findOneByPolynomialIdAndValue(
                            polynomial.getSimplifiedPolynomialHash(),
                            polynomial.getCalculationResult().getValue());

            if (polynomialCalculateEntity != null) {
                polynomial.getCalculationResult().setResult(polynomialCalculateEntity.getResult());
                polynomial.getCalculationResult().setCalculationResultSource(DATABASE);

                log.info("Database has evaluated result: result=\"{}\". [polynomial=\"{}\", value=\"{}\"]",
                        polynomial.getCalculationResult().getResult(), polynomial.getSimplifiedPolynomial(),
                        polynomial.getCalculationResult().getValue());

            } else {
                log.info("Evaluate polynomial for value: [polynomial=\"{}\", value=\"{}\"]",
                        polynomial.getSimplifiedPolynomial(), polynomial.getCalculationResult().getValue());

                polynomialProcessor.evaluatePolynomial(polynomial);
                polynomial.getCalculationResult().setCalculationResultSource(EVALUATION);

                log.info("Save evaluated result to DataBase: [result=\"{}\"]",
                        polynomial.getCalculationResult().getResult());

                polynomialRepository.save(
                        polynomialMapper.toEntity(polynomial));
            }

            log.info("Cache result after retrieve from DataBase or do evaluation process: " +
                    "[polynomial=\"{}\", value=\"{}\", result=\"{}\"]", polynomial.getSimplifiedPolynomial(),
                    polynomial.getCalculationResult().getValue(), polynomial.getCalculationResult().getResult());

            redisTemplate.opsForValue().set(cacheKey, polynomial.getCalculationResult().getResult(),
                    cacheProperties.getTtl(), cacheProperties.getTimeUnit());
        }
    }
}
