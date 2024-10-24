package com.example.polynomial.util.processor.converter;

import com.example.polynomial.exception.ConverterUnexpectedCaseException;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.polynomial.util.PolynomialRegEx.REGEX_POLYNOMIAL_SPLIT_ON_TERMS;
import static com.example.polynomial.util.PolynomialRegEx.REGEX_POLYNOMIAL_SPLIT_TERM_ON_COEFFICIENT_AND_VARIABLE;
import static com.example.polynomial.util.PolynomialRegEx.REGEX_POLYNOMIAL_SPLIT_TERM_VARIABLE_ON_X_AND_DEGREE;
import static com.example.polynomial.util.PolynomialRegEx.REGEX_POLYNOMIAL_TERM_COEFFICIENT;
import static com.example.polynomial.util.PolynomialRegEx.REGEX_POLYNOMIAL_TERM_VARIABLE_WITHOUT_DEGREE;
import static com.example.polynomial.util.PolynomialRegEx.REGEX_POLYNOMIAL_TERM_VARIABLE_WITH_DEGREE;
import static java.util.function.Predicate.not;

@Log4j2
@Component
public class PolynomialConverter {
    private static final Integer POLYNOMIAL_TERM_VARIABLE_DEGREE_EQ_ONE = 1;
    private static final Integer POLYNOMIAL_TERM_VARIABLE_DEGREE_EQ_ZERO = 0;
    private static final Integer POLYNOMIAL_TERM_COEFFICIENT_EQ_ONE = 1;

    public String toPolynomialAsString(List<Integer> coefficients) {
        Assert.isTrue(coefficients != null && !coefficients.isEmpty(), "Coefficients of polynomial must not be null or empty");
        Assert.isTrue(!coefficients.stream().allMatch(coefficient -> coefficient == 0), "All coefficients must not be equal to zero");

        log.info(">>> CONVERTER: Convert coefficients to polynomial - [ coefficients={} ]", coefficients);
        StringBuilder polynomialBuilder = new StringBuilder();
        for (int degree = coefficients.size() - 1; degree >= 0; degree--) {
            Integer coefficient = coefficients.get(degree);
            if (coefficient == 0) continue;
            if (degree == 0) {
                polynomialBuilder.append(coefficient < 0 ? coefficient : "+" + coefficient);
            } else {
                builderAppendHelper(polynomialBuilder, coefficient, degree);
            }
        }

        if ("+".equals(polynomialBuilder.substring(0, 1))) {
            polynomialBuilder.deleteCharAt(0);
        }

        String strPolynomial = polynomialBuilder.toString();
        log.info(">>> CONVERTER: Result converted coefficients to polynomial - [ polynomial={} ]", strPolynomial);

        return strPolynomial;
    }

    public List<String> toListTerms(String strPolynomial) {
        Assert.isTrue(StringUtils.hasText(strPolynomial), "Polynomial must not be null or empty");

        log.info(">>> CONVERTER: Convert polynomial to list of terms - [ polynomial=\"{}\" ]", strPolynomial);

        String [] arrayTerms = strPolynomial.replace("-", "+-")
                .split(REGEX_POLYNOMIAL_SPLIT_ON_TERMS);

        List<String> listTerms = Arrays.stream(arrayTerms)
                .filter(not(String::isEmpty))
                .toList();

        log.info(">>> CONVERTER: Result converted polynomial to list of terms - [ listTerms=\"{}\" ]", listTerms);

        return listTerms;
    }

    public Map<Integer, Integer> toMapDegreeTerms(List<String> terms) {
        Assert.isTrue(terms != null && !terms.isEmpty(),
                "List of polynomial terms must not be null or empty");

        log.info(">>> CONVERTER: Convert terms to degree terms - [ terms=\"{}\" ]", terms);

        Map<Integer, Integer> mapDegreeTerms = new HashMap<>();
        for (String term : terms) {
            String [] pairCoefVarOfTerm = term.split(REGEX_POLYNOMIAL_SPLIT_TERM_ON_COEFFICIENT_AND_VARIABLE);
            addToMapDegreeTerms(mapDegreeTerms, pairCoefVarOfTerm);
        }

        log.info(">>> CONVERTER: Result converted terms to degree terms - [ degreeTerms={} ]", mapDegreeTerms);

        return mapDegreeTerms;
    }

    private void addToMapDegreeTerms(Map<Integer, Integer> mapDegreeTerms, String [] pairCoefVarOfTerm) {
        switch (pairCoefVarOfTerm.length) {
            case 1 -> {
                Integer degree;
                Integer number;

                String numOrVar = pairCoefVarOfTerm[0];
                if (numOrVar.matches(REGEX_POLYNOMIAL_TERM_VARIABLE_WITHOUT_DEGREE)) {
                    degree = POLYNOMIAL_TERM_VARIABLE_DEGREE_EQ_ONE;
                    number = POLYNOMIAL_TERM_COEFFICIENT_EQ_ONE * (numOrVar.startsWith("-") ? -1 : 1);

                } else if (numOrVar.matches(REGEX_POLYNOMIAL_TERM_VARIABLE_WITH_DEGREE)) {
                    String [] pairVarXDegree = numOrVar.split(REGEX_POLYNOMIAL_SPLIT_TERM_VARIABLE_ON_X_AND_DEGREE);
                    degree = Integer.parseInt(pairVarXDegree[1]);
                    number = POLYNOMIAL_TERM_COEFFICIENT_EQ_ONE * (numOrVar.startsWith("-") ? -1 : 1);

                } else if (numOrVar.matches(REGEX_POLYNOMIAL_TERM_COEFFICIENT)) {
                    degree = POLYNOMIAL_TERM_VARIABLE_DEGREE_EQ_ZERO;
                    number = Integer.parseInt(numOrVar);

                } else {
                    // todo: we shouldn't come here
                    // todo: throw exception in unexpected case
                    throw new ConverterUnexpectedCaseException();
                }

                mapDegreeTerms.put(degree, mapDegreeTerms.getOrDefault(degree, 0) + number);
            }

            case 2 -> {
                Integer degree;
                Integer number = Integer.parseInt(pairCoefVarOfTerm[0]);

                if (pairCoefVarOfTerm[1].matches(REGEX_POLYNOMIAL_TERM_VARIABLE_WITHOUT_DEGREE)) {
                    degree = POLYNOMIAL_TERM_VARIABLE_DEGREE_EQ_ONE;

                } else if (pairCoefVarOfTerm[1].matches(REGEX_POLYNOMIAL_TERM_VARIABLE_WITH_DEGREE)){
                    String [] pairVarXDegree = pairCoefVarOfTerm[1].split(REGEX_POLYNOMIAL_SPLIT_TERM_VARIABLE_ON_X_AND_DEGREE);
                    degree = Integer.parseInt(pairVarXDegree[1]);

                } else {
                    // todo: we shouldn't come here
                    // todo: throw exception in unexpected case
                    throw new ConverterUnexpectedCaseException();
                }

                mapDegreeTerms.put(degree, mapDegreeTerms.getOrDefault(degree, 0) + number);
            }
        }
    }

    private void builderAppendHelper(StringBuilder builder, Integer coefficient, int degree) {
        if (coefficient < 0) {
            builder.append(coefficient == -1 ? "-" : coefficient);
        } else {
            builder.append(coefficient == 1 ? "+" : "+" + coefficient);
        }
        builder.append(Math.abs(coefficient) == 1 ? "x" : "*x")
                .append(degree == 1 ? "" : "^" + degree);
    }
}
