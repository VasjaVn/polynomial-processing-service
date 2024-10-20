package com.example.polynomial.util;

public class MultiplyPolynomialsTestDELETE {
    public static void main(String[] args) {

    }


    /**
     * Input:  A[] = {5, 0, 10, 6}
     *         B[] = {1, 2, 4}
     * Output: prod[] = {5, 10, 30, 26, 52, 24}
     *
     * The first input array represents "5 + 0x^1 + 10x^2 + 6x^3"
     * The second array represents "1 + 2x^1 + 4x^2"
     * And Output is "5 + 10x^1 + 30x^2 + 26x^3 + 52x^4 + 24x^5"
     */

    /**
     * First rawPolynomial is 5 + 0x^1 + 10x^2 + 6x^3
     * Second rawPolynomial is 1 + 2x^1 + 4x^2
     * Product rawPolynomial is 5 + 10x^1 + 30x^2 + 26x^3 + 52x^4 + 24x^5
     */

    // Java program to multiply two polynomials
    static class GFG
    {

        // A[] represents coefficients
        // of first rawPolynomial
        // B[] represents coefficients
        // of second rawPolynomial
        // m and n are sizes of A[] and B[] respectively
        static int[] multiply(int A[], int B[],
                              int m, int n)
        {
            int[] prod = new int[m + n - 1];

            // Initialize the product rawPolynomial
            for (int i = 0; i < m + n - 1; i++)
            {
                prod[i] = 0;
            }

            // Multiply two polynomials term by term
            // Take ever term of first rawPolynomial
            for (int i = 0; i < m; i++)
            {
                // Multiply the current term of first rawPolynomial
                // with every term of second rawPolynomial.
                for (int j = 0; j < n; j++)
                {
                    prod[i + j] += A[i] * B[j];
                }
            }

            return prod;
        }

        // A utility function to print a rawPolynomial
        static void printPoly(int poly[], int n)
        {
            for (int i = 0; i < n; i++)
            {
                System.out.print(poly[i]);
                if (i != 0)
                {
                    System.out.print("x^" + i);
                }
                if (i != n - 1)
                {
                    System.out.print(" + ");
                }
            }
        }

        // Driver code
        public static void main(String[] args)
        {
            // The following array represents
            // rawPolynomial 5 + 10x^2 + 6x^3
            int A[] = {5, 0, 10, 6};

            // The following array represents
            // rawPolynomial 1 + 2x + 4x^2
            int B[] = {1, 2, 4};
            int m = A.length;
            int n = B.length;

            System.out.println("First rawPolynomial is n");
            printPoly(A, m);
            System.out.println("nSecond rawPolynomial is n");
            printPoly(B, n);

            int[] prod = multiply(A, B, m, n);

            System.out.println("nProduct rawPolynomial is n");
            printPoly(prod, m + n - 1);
        }
    }
}
