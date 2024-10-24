package com.example.polynomial.service.impl;

import com.example.polynomial.config.app.props.CacheProperties;
import com.example.polynomial.exception.PolynomialNotFoundException;
import com.example.polynomial.model.entity.PolynomialCalculateEntity;
import com.example.polynomial.repository.PolynomialCalculateRepository;
import com.example.polynomial.util.cache.KeyCacheGenerator;
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

import static com.example.polynomial.model.domain.CalculationResultSource.CACHE;
import static com.example.polynomial.model.domain.CalculationResultSource.DATABASE;
import static com.example.polynomial.model.domain.CalculationResultSource.EVALUATION;
import static java.lang.Boolean.TRUE;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class PolynomialServiceImpl implements PolynomialService {

    private final CacheProperties cacheProperties;

    private final KeyCacheGenerator keyCacheGenerator;

    private final RedisTemplate<String, Double> redisTemplate;

    private final PolynomialRepository polynomialRepository;

    private final PolynomialCalculateRepository polynomialCalculateRepository;

    private final PolynomialProcessor polynomialProcessor;

    private final PolynomialMapper polynomialMapper;

    @Override
    public PolynomialResponseDto evaluatePolynomial(PolynomialRequestDto polynomialRequestDto) {
        Polynomial polynomial = polynomialMapper.toDomain(polynomialRequestDto);
        doEvaluatePolynomial(polynomial);

        return polynomialMapper.toDto(polynomial);
    }

    @Override
    public PolynomialResponseDto evaluatePolynomial(String rawPolynomial, Integer value) {
        Polynomial polynomial = polynomialMapper.toDomain(rawPolynomial);
        polynomial.getCalculationResult().setValue(value);

        doEvaluatePolynomial(polynomial);

        return polynomialMapper.toDto(polynomial);
    }

    @Override
    public PolynomialResponseDto deletePolynomial(String rawPolynomial) {
        Polynomial polynomial = polynomialMapper.toDomain(rawPolynomial);

        doSimplifyOrMultiplyPolynomial(polynomial);

        Integer polynomialId = polynomial.getSimplifiedPolynomialHash();

        polynomialRepository.findById(polynomialId)
                .orElseThrow(() -> new PolynomialNotFoundException(
                        "Polynomial not found for deleting in DataBase: [ polynomial=\"" + rawPolynomial + "\"]"));

        polynomialRepository.deleteById(polynomialId);
        log.info("Polynomial \"{}\" was deleted.", rawPolynomial);

        return polynomialMapper.toDtoWithoutCalculationResult(polynomial);
    }

    private void doSimplifyOrMultiplyPolynomial(Polynomial polynomial) {
        if (polynomial.hasMultipliers()) {
            log.info("Polynomial has multipliers --> do multiplied for polynomial: [ polynomial=\"{}\" ]", polynomial.getNormalized());
            polynomialProcessor.multiplyPolynomials(polynomial);
        } else {
            log.info("Polynomial is simple --> do simplified for polynomial: [ polynomial=\"{}\"] ", polynomial.getNormalized());
            polynomialProcessor.simplifyPolynomial(polynomial);
        }
    }

    private void doEvaluatePolynomial(Polynomial polynomial) {
        boolean isCacheMiss = true;

        doSimplifyOrMultiplyPolynomial(polynomial);

        String cacheKey = "";
        if (cacheProperties.isEnableCache()) {
            cacheKey = keyCacheGenerator.generateKeyFromPolynomial(polynomial);

            log.info("Generate cache key: cacheKey=\"{}\". [polynomial=\"{}\", value=\"{}\"] ",
                    cacheKey, polynomial.getSimplifiedPolynomial(), polynomial.getCalculationResult().getValue());

            if (TRUE.equals(redisTemplate.hasKey(cacheKey))) {
                Double cacheResult = redisTemplate.opsForValue().get(cacheKey);

                polynomial.getCalculationResult().setResult(cacheResult);
                polynomial.getCalculationResult().setCalculationResultSource(CACHE);

                isCacheMiss = false;

                log.info("Cache has cached result \"{}\" for key=\"{}\". [polynomial=\"{}\"]",
                        cacheResult, cacheKey, polynomial.getSimplifiedPolynomial());
            }
        }

        StringBuilder logMessageBuilder = new StringBuilder("Add result to Cache: ");
        if (isCacheMiss) {
            PolynomialCalculateEntity polynomialCalculateEntity =
                    polynomialCalculateRepository.findOneByPolynomialIdAndValue(
                            polynomial.getSimplifiedPolynomialHash(),
                            polynomial.getCalculationResult().getValue());

            if (polynomialCalculateEntity != null) {
                polynomial.getCalculationResult().setResult(polynomialCalculateEntity.getResult());
                polynomial.getCalculationResult().setCalculationResultSource(DATABASE);

                logMessageBuilder.append("result was retrieved from DataBase");

                log.info("Result was retrieved from DataBase: result={}. [ polynomial=\"{}\", value={} ]",
                        polynomial.getCalculationResult().getResult(), polynomial.getSimplifiedPolynomial(),
                        polynomial.getCalculationResult().getValue());

            } else {
                polynomial.getCalculationResult().setCalculationResultSource(EVALUATION);

                polynomialProcessor.evaluatePolynomial(polynomial);
                log.info("Result was evaluated: result={}. [ polynomial=\"{}\", value={} ]",
                        polynomial.getCalculationResult().getResult(), polynomial.getSimplifiedPolynomial(),
                        polynomial.getCalculationResult().getValue());

                polynomialRepository.save(
                        polynomialMapper.toEntity(polynomial));
                log.info("Save evaluated result to DataBase: [ result={} ]",
                        polynomial.getCalculationResult().getResult());

                logMessageBuilder.append("result was evaluated");
            }

            if (cacheProperties.isEnableCache()) {
                log.info("{} [polynomial=\"{}\", value=\"{}\", result=\"{}\"]", logMessageBuilder.toString(),
                        polynomial.getSimplifiedPolynomial(), polynomial.getCalculationResult().getValue(),
                        polynomial.getCalculationResult().getResult());

                redisTemplate.opsForValue().set(cacheKey, polynomial.getCalculationResult().getResult(),
                        cacheProperties.getTtl(), cacheProperties.getTimeUnit());
            }
        }
    }
}
