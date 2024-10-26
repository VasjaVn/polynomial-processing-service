package com.example.polynomial.repository;

import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {"/sql/schema.sql", "/sql/data.sql"})
public class RepositoryBaseIT {
    public static final int POLYNOMIAL_ID = 2014176811;
    public static final int POLYNOMIAL_ID_IS_ABSENT = 1111111;

    public static final int POLYNOMIAL_CALCULATE_ID = 1;
    public static final int POLYNOMIAL_CALCULATE_ID_IS_ABSENT = 2222222;

    public static final String POLYNOMIAL = "x^2+2*x-10";

    public static final int VALUE = 1;
    public static final int VALUE_IS_ABSENT = 2;

    public static final double RESULT = -7.0;
}
