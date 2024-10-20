package com.example.polynomial.util.processor.helper;

import com.example.polynomial.model.domain.Polynomial;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PolynomialEvaluator {

    public void evaluate(Polynomial polynomial) {
        Map<Integer,Integer> mapDegreeTerms = polynomial.getAnatomy().getMapDegreeTerms();
        Integer value = polynomial.getCalculationResult().getValue();
        double result = 0.0;
        for (Map.Entry<Integer,Integer> degreeTerm : mapDegreeTerms.entrySet()) {
            result += degreeTerm.getValue() * Math.pow(value, degreeTerm.getKey());
        }
        polynomial.getCalculationResult().setResult(result);
    }
}
