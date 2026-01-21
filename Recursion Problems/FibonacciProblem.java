/*
Fibonacci Sequence: Implement a recursive function to find the nth Fibonacci number.

This program prints the nth Fibonacci number, where fibonacci(n) gives the number.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 12/16/2025
*/


public class FibonacciProblem {
    public static int fibonacci(int n) {
        if (n == 1) {
            return 0;
        } else if (n == 2) {
            return 1;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    public static void main(String[] args) {
        System.out.println(fibonacci(5) + "");
    }
}
