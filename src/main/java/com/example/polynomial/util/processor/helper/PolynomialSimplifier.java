package com.example.polynomial.util.processor.helper;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.converter.PolynomialConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.TreeMap;

@Log4j2
@Component
public class PolynomialSimplifier extends AbstractPolynomialBase {

    public PolynomialSimplifier(PolynomialConverter polynomialConverter) {
        super(polynomialConverter);
    }

    @Override
    protected String getNormalizedOrMultipliedPolynomial(Polynomial polynomial) {
        return polynomial.getNormalized();
    }

    public void simplify(Polynomial polynomial) {
        Assert.isTrue(polynomial != null, "Polynomial must not be NULL");
        Assert.isTrue(polynomial.getAnatomy() != null, "Anatomy of Polynomial must not be NULL");

        log.info(">> SIMPLIFIER: Simplify polynomial - [ polynomial=\"{}\" ]", polynomial.getNormalized());

        evaluateAnatomyOfPolynomial(polynomial);
        doSimplifiedPolynomial(polynomial);

        log.info(">> SIMPLIFIER: Simplified polynomial - [ simplifiedPolynomial=\"{}\" ]", polynomial.getSimplified());
    }

    private void doSimplifiedPolynomial(Polynomial polynomial) {
        TreeMap<Integer, Integer> treeDegreeTerms =
                new TreeMap<>(polynomial.getAnatomy().getMapDegreeCoeff());

        StringBuilder simplifiedBuilder = new StringBuilder();
        for (Map.Entry<Integer, Integer> e : treeDegreeTerms.descendingMap().entrySet()) {
            if (e.getValue() == 0) continue;

            switch (e.getKey()) {
                case 0 -> simplifiedBuilder.append(e.getValue() > 0 ? "+" + e.getValue() : e.getValue());

                case 1 -> appendToSimplifiedBuilder(simplifiedBuilder, e.getValue());

                default -> {
                    appendToSimplifiedBuilder(simplifiedBuilder, e.getValue());
                    simplifiedBuilder.append("^").append(e.getKey());
                }
            }
        }

        if ("+".equals(simplifiedBuilder.substring(0,1))) {
            simplifiedBuilder.deleteCharAt(0);
        }

        polynomial.setSimplified(simplifiedBuilder.toString());
    }

    private void appendToSimplifiedBuilder(StringBuilder simplifiedBuilder, Integer value) {
        if (value < 0) {
            simplifiedBuilder.append(value == -1 ? "-" : value);
        } else {
            simplifiedBuilder.append(value == 1 ? "+" : "+" + value);
        }
        simplifiedBuilder.append(Math.abs(value) == 1 ? "x" : "*x");
    }
}
