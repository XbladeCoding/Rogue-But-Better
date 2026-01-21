/*
Factorial Calculation: Write a recursive function to calculate the factorial of a given number.

This program takes an integer, n, and prints the factorial n! .

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 12/16/2025
*/

public class FactorialProblem {
    public static int factorial(int n) {
        if (n == 1) {
            return 1;
        }
        return n * factorial(n-1);
    }
    public static void main(String[] args) {
        System.out.println(factorial(5));
    }
}
