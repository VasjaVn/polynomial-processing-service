package com.example.polynomial.util.cache;

import com.example.polynomial.config.app.props.CacheProperties;
import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.PolynomialProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class CacheWarmer implements CommandLineRunner {

    private final CacheProperties cacheProperties;

    private final KeyCacheGenerator keyCacheGenerator;

    private final RedisTemplate<String, Double> redisTemplate;

    private final PolynomialProcessor polynomialProcessor;

    @Override
    public void run(String... args) {

        if (cacheProperties.isEnableCache() && cacheProperties.isWarm()) {
            log.info("Redis Cache warming: START.");

            if (cacheProperties.getWarmData().isEmpty()) {
                log.info(" >>> WARM DATA: is empty.");

            } else {
                cacheProperties.getWarmData().forEach(warmData -> {
                    log.info(" >>> WARM DATA: [polynomial = \"{}\", value = {}]",
                            warmData.getPolynomial(), warmData.getValue());

                    Polynomial.CalculationResult calculationResult = new Polynomial.CalculationResult();
                    calculationResult.setValue(warmData.getValue());

                    Polynomial polynomial = new Polynomial(warmData.getPolynomial());
                    polynomial.setCalculationResult(calculationResult);

                    if (polynomial.hasMultipliers()) {
                        polynomialProcessor.multiplyPolynomials(polynomial);
                    } else {
                        polynomialProcessor.simplifyPolynomial(polynomial);
                    }

                    polynomialProcessor.evaluatePolynomial(polynomial);

                    String cacheKey = keyCacheGenerator.generateKeyFromPolynomial(polynomial);
                    redisTemplate.opsForValue().set(cacheKey, polynomial.getCalculationResult().getResult());
                });
            }
            log.info("Redis Cache warming: FINISHED.");

        } else {
            log.info("Start App without warning Redis Cache.");
        }
    }
}
