package com.example.polynomial.util.processor;

import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.util.processor.converter.PolynomialConverter;
import com.example.polynomial.util.processor.helper.PolynomialMultiplier;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.polynomial.util.PolynomialRegEx.REGEX_POLYNOMIAL_IS_VALID;
import static com.example.polynomial.util.PolynomialRegEx.REGEX_SM;
import static com.example.polynomial.util.PolynomialRegEx.REGEX_SSMM;
import static com.example.polynomial.util.PolynomialRegEx.REGEX_TEST;

public class PolynomialMultiplierTest {

    private final PolynomialConverter converter = new PolynomialConverter();

    private final PolynomialMultiplier polynomialMultiplier = new PolynomialMultiplier(converter);

    @Test
    public void test() {
        List<String> multipliers = List.of("2*x^2+3*x+5", "x+1", "-3*x^5-1");
//        String raw = "(2*x^2+3*x+5)*(x+1)*(-3*x^5-1)";
        String raw = "(x+2)*(x-1)";
//        String raw = "(-x+2)*(x-1)";
        Polynomial polynomial = Polynomial.builder().raw(raw).build();
        polynomialMultiplier.multiply(polynomial);
        System.out.println(polynomial.getMultiplied());
    }

    @Test
    public void test2() {
        String p = "2*x^2+x-2+2*x";
        boolean b = p.matches(REGEX_POLYNOMIAL_IS_VALID);
        System.out.println(b);
    }

    @Test
    public void test3() {
        String p = "(2*x^2+x-2+2*x)*(2+x)";
        boolean b = p.matches(REGEX_TEST);
        System.out.println(b);
    }

    @Test
    public void test4() {
        String p = "2*x^2+x-2+2*x";
        boolean b = p.matches(REGEX_SM);
        System.out.println(b);
    }

    @Test
    public void test5() {
        String p = "2 *x ^2+x-   2+2*x   ";
        boolean b = p.matches(REGEX_SSMM);
        System.out.println(b);
    }
}
