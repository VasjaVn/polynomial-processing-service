package com.example.polynomial.util;

import com.example.polynomial.model.domain.Polynomial;

public class Utils {

    public static String generateKeyForCacheFromPolynomial(Polynomial polynomial) {
        return polynomial.getSimplifiedPolynomial() + "|" + polynomial.getCalculationResult().getValue();
    }
}
