package com.example.polynomial.util.processor.helper;

import com.example.polynomial.model.domain.Polynomial;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component
public class PolynomialEvaluator {

    public void evaluate(Polynomial polynomial) {
        log.info(">> EVALUATOR: Evaluate polynomial - [ polynomial=\"{}\", value={} ].",
                polynomial.getSimplifiedPolynomial(), polynomial.getCalculationResult().getValue());

        Map<Integer,Integer> mapDegreeTerms = polynomial.getAnatomy().getMapDegreeTerms();
        Integer value = polynomial.getCalculationResult().getValue();

        double result = 0.0;
        for (Map.Entry<Integer,Integer> degreeTerm : mapDegreeTerms.entrySet()) {
            result += degreeTerm.getValue() * Math.pow(value, degreeTerm.getKey());
        }

        polynomial.getCalculationResult().setResult(result);
        log.info(">> EVALUATOR: Evaluated polynomial - [ result={} ].", result);
    }
}
