package com.example.polynomial.util.processor.helper;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.converter.PolynomialConverter;

import java.util.List;
import java.util.Map;

public abstract class AbstractPolynomialBase {

    protected final PolynomialConverter polynomialConverter;

    public AbstractPolynomialBase(PolynomialConverter polynomialConverter) {
        this.polynomialConverter = polynomialConverter;
    }

    protected void evaluateAnatomyForPolynomial(Polynomial polynomial) {
        Polynomial.Anatomy polynomialAnatomy = polynomial.getAnatomy();

        List<String> listTerms =
                polynomialConverter.toListTerms(getNormalizedOrMultipliedPolynomial(polynomial));
        polynomialAnatomy.setListTerms(listTerms);

        Map<Integer, Integer> mapDegreeTerms = polynomialConverter.toMapDegreeTerms(listTerms);
        polynomialAnatomy.setMapDegreeTerms(mapDegreeTerms);
    }

    protected abstract String getNormalizedOrMultipliedPolynomial(Polynomial polynomial);
}
