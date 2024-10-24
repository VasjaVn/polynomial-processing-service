package com.example.polynomial.util.processor.helper;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.converter.PolynomialConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class PolynomialMultiplier extends AbstractPolynomialBase {

    public PolynomialMultiplier(PolynomialConverter polynomialConverter) {
        super(polynomialConverter);
    }

    @Override
    protected String getNormalizedOrMultipliedPolynomial(Polynomial polynomial) {
        return polynomial.getMultiplied();
    }

    public void multiply(Polynomial polynomial) {
        Assert.isTrue(polynomial != null, "Polynomial must not be NULL");
        Assert.isTrue(polynomial.getAnatomy() != null, "Anatomy of Polynomial must not be NULL");

        List<String> multipliers = polynomial.getMultipliers();

        log.info(">> MULTIPLIER: Multiply polynomial - [ polynomial=\"{}\", multipliers={} ].",
                polynomial.getNormalized(), multipliers);

        List<Integer> firstCoefficients = preparePolynomialForMultiply(multipliers.get(0));
        List<Integer> secondCoefficients = preparePolynomialForMultiply(multipliers.get(1));
        List<Integer> resultCoefficients = multiplyHelper(firstCoefficients, secondCoefficients);

        for (int i = 2; i< multipliers.size(); i++) {
            List<Integer> nextCoefficients = preparePolynomialForMultiply(multipliers.get(i));
            resultCoefficients = multiplyHelper(resultCoefficients, nextCoefficients);
        }

        polynomial.setMultiplied(
                polynomialConverter.toPolynomialAsString(resultCoefficients));

        log.info(">> MULTIPLIER: Multiplied polynomial - [ multipliedPolynomial=\"{}\" ]", polynomial.getMultiplied());

        evaluateAnatomyOfPolynomial(polynomial);
    }

    private List<Integer> preparePolynomialForMultiply(String multiplier) {
        List<Integer> preparedMultiplier = new ArrayList<>();

        List<String> terms = polynomialConverter.toListTerms(multiplier);
        Map<Integer,Integer> mapDegreeTerms = polynomialConverter.toMapDegreeTerms(terms);

        int maxDegree = Collections.max(mapDegreeTerms.keySet());
        for (int degree = 0; degree <= maxDegree; degree++) {
            preparedMultiplier.add(mapDegreeTerms.getOrDefault(degree, 0));
        }

        return preparedMultiplier;
    }

    private List<Integer> multiplyHelper(List<Integer> first, List<Integer> second) {
        List<Integer> result = new ArrayList<>(
                Collections.nCopies(first.size() + second.size() - 1, 0));

        for (int i = 0; i < first.size(); i++) {
            for (int j = 0; j < second.size(); j++) {
                result.set(i + j, result.get(i + j) + (first.get(i) * second.get(j)));
            }
        }

        return result;
    }
}
