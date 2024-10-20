package com.example.polynomial.util.processor;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.converter.PolynomialConverter;
import com.example.polynomial.util.processor.helper.PolynomialEvaluator;
import com.example.polynomial.util.processor.helper.PolynomialMultiplier;
import com.example.polynomial.util.processor.helper.PolynomialSimplifier;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimplePolynomialProcessorTest {

    private final PolynomialConverter converter = new PolynomialConverter();

    private final PolynomialSimplifier simplifier = new PolynomialSimplifier(converter);

    private final PolynomialMultiplier multiplier = new PolynomialMultiplier(converter);

    private final PolynomialEvaluator evaluator = new PolynomialEvaluator();

    private final PolynomialProcessor polynomialProcessor =
            new PolynomialProcessor(multiplier, simplifier, evaluator);

    @Test
    public void test() {
//        Polynomial rawPolynomial = new Polynomial("2*x^2+3*x-5-x^3-3*x^2+5+10");
//        Polynomial rawPolynomial = new Polynomial("2*x^2+3*x-2*x^3-3*x^2-10+20");
        Polynomial polynomial = new Polynomial("-2*x^2-3*x-2*x^3-3*x^2-10-20-x-x+2*x^3");
        polynomialProcessor.simplifyPolynomial(polynomial);
    }

    @Test
    public void testMultiplied() {
//        Pattern pattern = Pattern.compile(REGEX_POLYNOMIAL_SPLIT_ON_MULTIPLIERS);
        Pattern pattern = Pattern.compile("\\(([^()]*?)\\)");

        Matcher matcher = pattern.matcher("(2*x^2+3*x-5-x^3-3*x^2+5+10)*(x-1)*(x+2)*(x+1)");
        System.out.println(matcher.groupCount());
        while (matcher.find()) {
            System.out.println("YES");
        }
    }

    @Test
    public void test2() {
//        String input = "Приклад: (3x^2 + 2x - 1) і (x^3 - 4x + 5)";
//        String input = "(3*x^2 + 2*x - 1)*(x^3 - 4*x + 5)*(1 - x)";
        String input = "3*x^2 + 2*x - 1";

        // Регулярний вираз для виділення поліномів
        String regex = "\\(([^()]*?)\\)|([^()]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Виводимо всі знайдені поліноми
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                System.out.println("Знайдений поліном: " + matcher.group(1).trim());
            }

            System.out.println(matcher.toString());
        }
    }
}
