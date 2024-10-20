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
    //^\s*(\d*\.?\d*?x\^?\d*|\d*\.?\d*?x|\d*\.?\d+|\(\s*(\d*\.?\d*?x\^?\d*|\d*\.?\d*?x|\d*\.?\d+)\s*(\*\s*(\d*\.?\d*?x\^?\d*|\d*\.?\d*?x|\d*\.?\d+))*(\s*\*\s*\(\s*(\d*\.?\d*?x\^?\d*|\d*\.?\d*?x|\d*\.?\d+)\s*\))*\s*\))*(\s*\*\s*(\d*\.?\d*?x\^?\d*|\d*\.?\d*?x|\d*\.?\d+))*)*\s*$
//    public static final String REGEX_POLYNOMIAL_IS_VALID = "^\\s*([+-]?\\d+x(\\^\\d+)?|[+-]?\\d+|(\\(\\s*.*?\\s*\\)))\\s*(\\*\\s*([+-]?\\d+x(\\^\\d+)?|[+-]?\\d+|(\\(\\s*.*?\\s*\\))))*\\s*$\n";
//    public static final String REGEX_POLYNOMIAL_IS_VALID = "^([-+]?\\d*x\\^\\d+|[-+]?\\d*x|[-+]?\\d+)(\\s*[-+]\\s*([-+]?\\d*x\\^\\d+|[-+]?\\d*x|[-+]?\\d+))*$";
    public static final String REGEX_POLYNOMIAL_IS_VALID = "^([+-]?\\d+\\*[xX]\\^\\d+|[+-]?[xX]\\^\\d+|[+-]?[xX]|[+-]?\\d+\\*[xX]|[+-]?\\d+)*$";
    public static final String REGEX_TEST = "^(\\*?\\(([+-]?\\d+\\*[xX]\\^\\d+|[+-]?[xX]\\^\\d+|[+-]?[xX]|[+-]?\\d+\\*[xX]|[+-]?\\d+)*\\))*$";


    public static final String REGEX_S = "([+-]?\\d+\\*[xX]\\^\\d+|[+-]?[xX]\\^\\d+|[+-]?[xX]|[+-]?\\d+\\*[xX]|[+-]?\\d+)*";
    public static final String REGEX_M = "(\\*?\\(([+-]?\\d+\\*[xX]\\^\\d+|[+-]?[xX]\\^\\d+|[+-]?[xX]|[+-]?\\d+\\*[xX]|[+-]?\\d+)*\\))*";

    public static final String REGEX_SM = "^(" + REGEX_S + "|" + REGEX_M + ")*$";

    public static final String REGEX_POLYNOMIAL_VALID_CHARACTERS = "^[\\dx*^\\-+\\s]+$";
    public static final String REGEX_POLYNOMIAL_MULTIPLY_VALID_CHARACTERS = "^[\\dx*^\\-+\\s()]+$";

    public static final String REGEX_POLYNOMIAL_TERM_VARIABLE_WITHOUT_DEGREE = "^\\-?x$";
    public static final String REGEX_POLYNOMIAL_TERM_VARIABLE_WITH_DEGREE = "^\\-?x\\^\\d+$";

    public static final String REGEX_POLYNOMIAL_TERM_COEFFICIENT = "^\\s?\\-?\\s?\\d+\\s?$";

    public static final String REGEX_POLYNOMIAL_SPLIT_ON_TERMS = "\\+";
    public static final String REGEX_POLYNOMIAL_SPLIT_TERM_ON_COEFFICIENT_AND_VARIABLE = "\\*";
    public static final String REGEX_POLYNOMIAL_SPLIT_TERM_VARIABLE_ON_X_AND_DEGREE = "\\^";

    public static final String REGEX_POLYNOMIAL_SPLIT_ON_MULTIPLIERS_OR_SINGLE = "\\(([^()]*?)\\)|([^()]*)";
//    public static final String REGEX_POLYNOMIAL_SPLIT_ON_MULTIPLIERS = "^[-+]?\\d*\\*?x\\^?\\d*|[-+]?\\d+$";

    public static final String REGEX_SS = "(\\s*[+-]?\\s*\\d+\\s*\\*\\s*[xX]\\s*\\^\\s*\\d+\\s*|\\s*[+-]?\\s*[xX]\\s*\\^\\s*\\d+\\s*|\\s*[+-]?\\s*[xX]\\s*|\\s*[+-]?\\s*\\d+\\s*\\*\\s*[xX]\\s*|\\s*[+-]?\\s*\\d+\\s*)*";
    public static final String REGEX_MM = "(\\s*\\*?\\((\\s*[+-]?\\s*\\d+\\s*\\*\\s*[xX]\\s*\\^\\s*\\d+\\s*|\\s*[+-]?\\s*[xX]\\s*\\^\\s*\\d+\\s*|\\s*[+-]?\\s*[xX]\\s*|\\s*[+-]?\\s*\\d+\\s*\\*\\s*[xX]\\s*|\\s*[+-]?\\s*\\d+\\s*)*\\)\\s*)*";
    public static final String REGEX_SSMM = "^(" + REGEX_SS + "|" + REGEX_MM + ")*$";

}
