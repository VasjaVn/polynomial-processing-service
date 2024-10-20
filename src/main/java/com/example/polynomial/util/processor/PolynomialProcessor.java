package com.example.polynomial.util.processor;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.helper.PolynomialEvaluator;
import com.example.polynomial.util.processor.helper.PolynomialMultiplier;
import com.example.polynomial.util.processor.helper.PolynomialSimplifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PolynomialProcessor {

    private final PolynomialMultiplier polynomialMultiplier;

    private final PolynomialSimplifier polynomialSimplifier;

    private final PolynomialEvaluator polynomialEvaluator;

    public void simplifyPolynomial(Polynomial polynomial) {
        polynomialSimplifier.simplify(polynomial);
    }

    public void multiplyPolynomials(Polynomial polynomial) {
        if (polynomial.hasMultipliers()) {
            polynomialMultiplier.multiply(polynomial);
        }
    }

    public void evaluatePolynomial(Polynomial polynomial) {
        polynomialEvaluator.evaluate(polynomial);
    }
}
