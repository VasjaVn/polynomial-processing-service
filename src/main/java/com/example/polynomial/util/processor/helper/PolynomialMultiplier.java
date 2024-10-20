package com.example.polynomial.util.processor.helper;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.converter.PolynomialConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        List<String> multipliers = polynomial.getMultipliers();

        List<Integer> firstCoefficients = preparePolynomialForMultiply(multipliers.get(0));
        List<Integer> secondCoefficients = preparePolynomialForMultiply(multipliers.get(1));
        List<Integer> resultCoefficients = multiplyHelper(firstCoefficients, secondCoefficients);

        for (int i = 2; i< multipliers.size(); i++) {
            List<Integer> nextCoefficients = preparePolynomialForMultiply(multipliers.get(i));
            resultCoefficients = multiplyHelper(resultCoefficients, nextCoefficients);
        }

        polynomial.setMultiplied(
                polynomialConverter.toPolynomailAsString(resultCoefficients));
//        polynomial.setSimplified(polynomial.getMultiplied());

        evaluateAnatomyForPolynomial(polynomial);
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
                Collections.nCopies(first.size() + second.size() -1, 0));

        for (int i = 0; i < first.size(); i++) {
            for (int j = 0; j < second.size(); j++) {
                result.set(i + j, result.get(i + j) + (first.get(i) * second.get(j)));
            }
        }

        return result;
    }
}
