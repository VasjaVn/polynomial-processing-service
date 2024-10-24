package com.example.polynomial.util.cache;

import com.example.polynomial.model.domain.Polynomial;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class KeyCacheGenerator {

    public String generateKeyFromPolynomial(Polynomial polynomial) {
        Assert.isTrue(polynomial != null, "Polynomial must not be NULL");
        Assert.isTrue(polynomial.getSimplifiedPolynomial() != null, "Polynomial must not be NULL");
        Assert.isTrue(polynomial.getCalculationResult() != null,
                "Calculation result of Polynomial must not be NULL");
        Assert.isTrue(polynomial.getCalculationResult().getValue() != null,
                "Value of Calculation Result must not be NULL");

        return polynomial.getSimplifiedPolynomial() + "|" + polynomial.getCalculationResult().getValue();
    }
}
