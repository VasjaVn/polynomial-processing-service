package com.example.polynomial.util.processor.helper;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.converter.PolynomialConverter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;

@Log4j2
public abstract class AbstractPolynomialBase {

    protected final PolynomialConverter polynomialConverter;

    public AbstractPolynomialBase(PolynomialConverter polynomialConverter) {
        this.polynomialConverter = polynomialConverter;
    }

    protected void evaluateAnatomyOfPolynomial(Polynomial polynomial) {
        Polynomial.Anatomy polynomialAnatomy = polynomial.getAnatomy();

        List<String> listTerms =
                polynomialConverter.toListTerms(getNormalizedOrMultipliedPolynomial(polynomial));
        polynomialAnatomy.setListTerms(listTerms);

        log.info(">> Evaluate anatomy of polynomial: [ terms={} ]", listTerms);

        Map<Integer, Integer> mapDegreeCoeff = polynomialConverter.toMapDegreeCoeff(listTerms);
        polynomialAnatomy.setMapDegreeCoeff(mapDegreeCoeff);

        log.info(">> Evaluate anatomy of polynomial: [ degreeTerms={} ]", mapDegreeCoeff);
    }

    protected abstract String getNormalizedOrMultipliedPolynomial(Polynomial polynomial);
}
