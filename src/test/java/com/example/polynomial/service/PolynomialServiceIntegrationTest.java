package com.example.polynomial.service;

import com.example.polynomial.config.app.props.CacheProperties;
import com.example.polynomial.exception.PolynomialNotFoundException;
import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.model.dto.response.PolynomialResponseDto;
import com.example.polynomial.repository.PolynomialCalculateRepository;
import com.example.polynomial.repository.PolynomialRepository;
import com.example.polynomial.util.cache.KeyCacheGenerator;
import com.example.polynomial.util.mapper.PolynomialMapper;
import com.example.polynomial.util.processor.PolynomialProcessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import redis.embedded.RedisServer;

import static com.example.polynomial.model.domain.CalculationResultSource.CACHE;
import static com.example.polynomial.model.domain.CalculationResultSource.DATABASE;
import static com.example.polynomial.model.domain.CalculationResultSource.EVALUATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Sql(scripts = {"/sql/schema.sql", "/sql/data.sql"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "JWT_SECRET_KEY=AAAAAAAAAAAAAAAAAAAAAAAAAAAA",
        "ENABLE_CACHE=true"
})
public class PolynomialServiceIntegrationTest {

    private static final RedisServer REDIS_SERVER = new RedisServer();

    private static final String POLYNOMIAL_DOES_NOT_EXIST_IN_DB_AND_CACHE = "x^2+2*x-1000";
    private static final String POLYNOMIAL_EXISTS_IN_CACHE = "x^2+2*x-100";
    private static final String POLYNOMIAL_EXISTS_IN_DB = "x^2+2*x-10";

    private static final int VALUE = 1;

    @Autowired
    private PolynomialService polynomialService;

    @Autowired
    private CacheProperties cacheProperties;

    @SpyBean
    private PolynomialMapper polynomialMapper;

    @SpyBean
    private PolynomialProcessor polynomialProcessor;

    @SpyBean
    private RedisTemplate<String, Double> redisTemplate;

    @SpyBean
    private PolynomialRepository polynomialRepository;

    @SpyBean
    private PolynomialCalculateRepository polynomialCalculateRepository;

    @SpyBean
    private KeyCacheGenerator keyCacheGenerator;

    @BeforeAll
    public static void startRedis() {
        REDIS_SERVER.start();
    }

    @AfterAll
    public static void stopRedis() {
        REDIS_SERVER.stop();
    }

    @Test
    public void evaluatePolynomial_ResultDoesNotExistInCacheAndDb_ThenEvaluateResultAndAddToDbAndCache() {
        PolynomialResponseDto polynomialResponseDto =
                polynomialService.evaluatePolynomial(POLYNOMIAL_DOES_NOT_EXIST_IN_DB_AND_CACHE, VALUE);

        assertNotNull(polynomialResponseDto);
        assertEquals(POLYNOMIAL_DOES_NOT_EXIST_IN_DB_AND_CACHE, polynomialResponseDto.rawPolynomial());
        assertEquals(POLYNOMIAL_DOES_NOT_EXIST_IN_DB_AND_CACHE, polynomialResponseDto.simplifiedPolynomial());
        assertNull(polynomialResponseDto.multipliedPolynomial());
        assertNotNull(polynomialResponseDto.calculationResult());
        assertEquals(VALUE, polynomialResponseDto.calculationResult().getValue());
        assertEquals(-997.0, polynomialResponseDto.calculationResult().getResult());
        assertEquals(EVALUATION, polynomialResponseDto.calculationResult().getCalculationResultSource());

        verify(polynomialMapper).toDomain(anyString());

        if (new Polynomial(POLYNOMIAL_DOES_NOT_EXIST_IN_DB_AND_CACHE).hasMultipliers()) {
            verify(polynomialProcessor).multiplyPolynomials(any());
        } else {
            verify(polynomialProcessor).simplifyPolynomial(any());
        }

        if (cacheProperties.isEnableCache()) {
            verify(keyCacheGenerator).generateKeyFromPolynomial(any());
            verify(redisTemplate).hasKey(any());
        }

        verify(polynomialCalculateRepository).findOneByPolynomialIdAndValue(anyInt(), anyInt());
        verify(polynomialProcessor).evaluatePolynomial(any());
        verify(polynomialMapper).toEntity(any());
        verify(polynomialRepository).save(any());

        if (cacheProperties.isEnableCache()) {
            verify(redisTemplate).opsForValue();
        }

        verify(polynomialMapper).toDto(any());
    }

    @Test
    public void evaluatePolynomial_ResultExistsInCache_ThenReturnResultFromCache() {
        String cacheKey = POLYNOMIAL_EXISTS_IN_CACHE + "|" + VALUE;
        redisTemplate.opsForValue().set(cacheKey, -97.0); // in verification, we need to add 1

        PolynomialResponseDto polynomialResponseDto =
                polynomialService.evaluatePolynomial(POLYNOMIAL_EXISTS_IN_CACHE, VALUE);

        assertNotNull(polynomialResponseDto);
        assertEquals(POLYNOMIAL_EXISTS_IN_CACHE, polynomialResponseDto.rawPolynomial());
        assertEquals(POLYNOMIAL_EXISTS_IN_CACHE, polynomialResponseDto.simplifiedPolynomial());
        assertNull(polynomialResponseDto.multipliedPolynomial());
        assertNotNull(polynomialResponseDto.calculationResult());
        assertEquals(VALUE, polynomialResponseDto.calculationResult().getValue());
        assertEquals(-97.0, polynomialResponseDto.calculationResult().getResult());
        assertEquals(CACHE, polynomialResponseDto.calculationResult().getCalculationResultSource());

        verify(polynomialMapper).toDomain(anyString());

        if (new Polynomial(POLYNOMIAL_DOES_NOT_EXIST_IN_DB_AND_CACHE).hasMultipliers()) {
            verify(polynomialProcessor).multiplyPolynomials(any());
        } else {
            verify(polynomialProcessor).simplifyPolynomial(any());
        }

        if (cacheProperties.isEnableCache()) {
            verify(keyCacheGenerator).generateKeyFromPolynomial(any());
            verify(redisTemplate).hasKey(anyString());
            verify(redisTemplate, times(1+1)).opsForValue();
        }

        verify(polynomialMapper).toDto(any());
    }

    @Test
    public void evaluatePolynomial_ResultExistsInDb_ThenReturnResultFromDbAndAddToCache() {
        PolynomialResponseDto polynomialResponseDto =
                polynomialService.evaluatePolynomial(POLYNOMIAL_EXISTS_IN_DB, VALUE);

        assertNotNull(polynomialResponseDto);
        assertEquals(POLYNOMIAL_EXISTS_IN_DB, polynomialResponseDto.rawPolynomial());
        assertEquals(POLYNOMIAL_EXISTS_IN_DB, polynomialResponseDto.simplifiedPolynomial());
        assertNull(polynomialResponseDto.multipliedPolynomial());
        assertNotNull(polynomialResponseDto.calculationResult());
        assertEquals(VALUE, polynomialResponseDto.calculationResult().getValue());
        assertEquals(-7.0, polynomialResponseDto.calculationResult().getResult());
        assertEquals(DATABASE, polynomialResponseDto.calculationResult().getCalculationResultSource());

        verify(polynomialMapper).toDomain(anyString());

        if (new Polynomial(POLYNOMIAL_DOES_NOT_EXIST_IN_DB_AND_CACHE).hasMultipliers()) {
            verify(polynomialProcessor).multiplyPolynomials(any());
        } else {
            verify(polynomialProcessor).simplifyPolynomial(any());
        }

        if (cacheProperties.isEnableCache()) {
            verify(keyCacheGenerator).generateKeyFromPolynomial(any());
            verify(redisTemplate).hasKey(anyString());
        }

        verify(polynomialCalculateRepository).findOneByPolynomialIdAndValue(anyInt(), anyInt());

        if (cacheProperties.isEnableCache()) {
            verify(redisTemplate).opsForValue();
        }

        verify(polynomialMapper).toDto(any());
    }

    @Test
    public void deletePolynomial_PolynomialExistsInDB_ThenDeletePolynomial() {
        PolynomialResponseDto polynomialResponseDto =
                polynomialService.deletePolynomial(POLYNOMIAL_EXISTS_IN_DB);

        assertNotNull(polynomialResponseDto);
        assertEquals(POLYNOMIAL_EXISTS_IN_DB, polynomialResponseDto.rawPolynomial());
        assertEquals(POLYNOMIAL_EXISTS_IN_DB, polynomialResponseDto.simplifiedPolynomial());
        assertNull(polynomialResponseDto.multipliedPolynomial());
        assertNull(polynomialResponseDto.calculationResult());

        int polynomialId = POLYNOMIAL_EXISTS_IN_DB.hashCode();

        verify(polynomialMapper).toDomain(anyString());
        verify(polynomialRepository).findById(polynomialId);
        verify(polynomialRepository).deleteById(polynomialId);
        verify(polynomialMapper).toDtoWithoutCalculationResult(any());
    }

    @Test
    public void deletePolynomial_PolynomialDoesNotExistInDB_ThenThrowPolynomialNotFoundException() {
        assertThrows(PolynomialNotFoundException.class, () -> {
            polynomialService.deletePolynomial(POLYNOMIAL_DOES_NOT_EXIST_IN_DB_AND_CACHE);
        });

        int polynomialId = POLYNOMIAL_DOES_NOT_EXIST_IN_DB_AND_CACHE.hashCode();

        verify(polynomialMapper).toDomain(anyString());
        verify(polynomialRepository).findById(polynomialId);
        verify(polynomialRepository, times(0)).deleteById(polynomialId);
        verify(polynomialMapper, times(0)).toDtoWithoutCalculationResult(any());
    }
}
