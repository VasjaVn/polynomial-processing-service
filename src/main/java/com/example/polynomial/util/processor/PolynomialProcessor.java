package com.example.polynomial.util.processor;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.helper.PolynomialEvaluator;
import com.example.polynomial.util.processor.helper.PolynomialMultiplier;
import com.example.polynomial.util.processor.helper.PolynomialSimplifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Log4j2
@Component
@RequiredArgsConstructor
public class PolynomialProcessor {

    private final PolynomialMultiplier polynomialMultiplier;

    private final PolynomialSimplifier polynomialSimplifier;

    private final PolynomialEvaluator polynomialEvaluator;

    public void simplifyPolynomial(Polynomial polynomial) {
        log.info("> PROCESSOR: Simplify polynomial - [ polynomial=\"{}\" ]", polynomial.getNormalized());

        polynomialSimplifier.simplify(polynomial);

        log.info("> PROCESSOR: Simplified polynomial - [ simplifiedPolynomial=\"{}\" ]",
                polynomial.getSimplified());
    }

    public void multiplyPolynomials(Polynomial polynomial) {
        Assert.isTrue(polynomial.hasMultipliers(), "Need two or more polynomial multipliers");

        log.info("> PROCESSOR: Multiply polynomial - [ polynomial=\"{}\" ]", polynomial.getNormalized());

        polynomialMultiplier.multiply(polynomial);

        log.info("> PROCESSOR: Multiplied polynomial - [ multipliedPolynomial=\"{}\" ]",
                polynomial.getMultiplied());
    }

    public void evaluatePolynomial(Polynomial polynomial) {
        log.info("> PROCESSOR: Evaluate polynomial - [ polynomial=\"{}\", value={} ]",
                polynomial.getSimplifiedPolynomial(), polynomial.getCalculationResult().getValue());

        polynomialEvaluator.evaluate(polynomial);

        log.info("> PROCESSOR: Evaluated polynomial - [ result={} ]",
                polynomial.getCalculationResult().getResult());
    }
}
