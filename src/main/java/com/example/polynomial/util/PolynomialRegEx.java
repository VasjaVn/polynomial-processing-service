package com.example.polynomial.util;

/********************************************************
 *    _______________________________                   *
 *      TERM    |     d REGEX                            *
 *    -------------------------------                   *
 *    +/-N*x^N -> [+-]?\d+\*[xX]\^\d+                   *
 *    +/-x^N   -> [+-]?[xX]\^\d+                        *
 *    +/-x     -> [+-]?[xX]                             *
 *    +/-N*x   -> [+-]?\d+\*[xX]                        *
 *    +/-N     -> [+-]?\d+                              *
 *    -------------------------------                   *
 *                                                      *
 *    {'+', '-', 'x', 'X', '*', '^', '(', ')', '0'}     *
 *    {'1', '2', '3', '4', '5', '6', '7', '8', '9'}     *
 *                                                      *
 ********************************************************/

public class PolynomialRegEx {

    public static final String REGEX_VALID_SINGLE_POLYNOMIAL = "(\\s*[+-]?\\s*\\d+\\s*\\*\\s*[xX]\\s*\\^\\s*\\d+\\s*|\\s*[+-]?\\s*[xX]\\s*\\^\\s*\\d+\\s*|\\s*[+-]?\\s*[xX]\\s*|\\s*[+-]?\\s*\\d+\\s*\\*\\s*[xX]\\s*|\\s*[+-]?\\s*\\d+\\s*)*";
    public static final String REGEX_VALID_MANY_MULTIPLIED_POLYNOMIALS = "(\\s*\\*?\\((\\s*[+-]?\\s*\\d+\\s*\\*\\s*[xX]\\s*\\^\\s*\\d+\\s*|\\s*[+-]?\\s*[xX]\\s*\\^\\s*\\d+\\s*|\\s*[+-]?\\s*[xX]\\s*|\\s*[+-]?\\s*\\d+\\s*\\*\\s*[xX]\\s*|\\s*[+-]?\\s*\\d+\\s*)*\\)\\s*)*";
    public static final String REGEX_VALID_POLYNOMIAL = "^(" + REGEX_VALID_SINGLE_POLYNOMIAL + "|" + REGEX_VALID_MANY_MULTIPLIED_POLYNOMIALS + ")*$";

    public static final String REGEX_POLYNOMIAL_TERM_VARIABLE_WITHOUT_DEGREE = "^\\-?x$";
    public static final String REGEX_POLYNOMIAL_TERM_VARIABLE_WITH_DEGREE = "^\\-?x\\^\\d+$";

    public static final String REGEX_POLYNOMIAL_TERM_COEFFICIENT = "^\\s?\\-?\\s?\\d+\\s?$";

    public static final String REGEX_POLYNOMIAL_SPLIT_ON_TERMS = "\\+";
    public static final String REGEX_POLYNOMIAL_SPLIT_TERM_ON_COEFFICIENT_AND_VARIABLE = "\\*";
    public static final String REGEX_POLYNOMIAL_SPLIT_TERM_VARIABLE_ON_X_AND_DEGREE = "\\^";

    public static final String REGEX_POLYNOMIAL_SPLIT_ON_MULTIPLIERS_OR_SINGLE = "\\(([^()]*?)\\)|([^()]*)";
}
