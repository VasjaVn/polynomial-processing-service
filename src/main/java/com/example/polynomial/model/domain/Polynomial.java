package com.example.polynomial.model.domain;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.polynomial.util.PolynomialRegEx.REGEX_POLYNOMIAL_SPLIT_ON_MULTIPLIERS_OR_SINGLE;

@Data
public class Polynomial {

    private final String raw;

    private final String normalized;

    private String simplified;

    private String multiplied;

    private CalculationResult calculationResult;

    private final List<String> multipliers;

    private Anatomy anatomy;

    private final Map<Integer, Double> cache;

    @Builder
    public static Polynomial of(String raw, String simplified, String multiplied,
                                CalculationResult calculationResult, Anatomy anatomy) {
        return new Polynomial(raw);
    }

    public Polynomial(String rawPolynomial) {
        this.raw = rawPolynomial;
        this.normalized = rawPolynomial.replace(" ", "").toLowerCase();
        this.calculationResult = new CalculationResult();
        this.multipliers = new ArrayList<>();
        this.anatomy = new Anatomy();
        this.cache = new HashMap<>();
        initialize();
    }

    public String getNormalized() {
        return multipliers.size() == 1 ? multipliers.get(0) : normalized;
    }

    public boolean hasMultipliers() {
        return multipliers.size() >= 2;
    }

    public String getSimplifiedPolynomial() {
        return simplified != null ? simplified : multiplied;
    }

    public Integer getSimplifiedPolynomialHash() {
        return getSimplifiedPolynomial().hashCode();
    }

//    public Double evaluateFor(int value) {
//        double result = 0.0;
//        if (anatomy.getMapDegreeTerms() == null) throw new IllegalStateException();
//
//        if (cache.containsKey(value)) {
//            return cache.get(value);
//        } else {
//            for (Map.Entry<Integer, Integer> e : anatomy.mapDegreeTerms.entrySet()) {
//                result += e.getValue() * Math.pow(value, e.getKey());
//            }
//            cache.put(value, result);
//        }
//
//        return result;
//    }

    private void initialize() {
        Pattern pattern = Pattern.compile(REGEX_POLYNOMIAL_SPLIT_ON_MULTIPLIERS_OR_SINGLE);
        Matcher matcher = pattern.matcher(normalized);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                multipliers.add(matcher.group(1));
            }
        }
    }

    @Data
    public static class Anatomy {
        private Map<Integer, Integer> mapDegreeTerms;
        private List<String> listTerms;
    }

    @Data
    public static class CalculationResult {
        private Integer value;
        private Double result;
        private CalculationResultSource calculationResultSource;
    }
}
