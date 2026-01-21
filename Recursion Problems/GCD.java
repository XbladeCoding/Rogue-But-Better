/*
Greatest Common Divisor (GCD): Write a recursive function to find the GCD of two numbers using Euclid's algorithm.

This code takes two numbers, a and b, and returns the greates common denomitator (gcd) using Euclid's Algorithm.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 12/17/2025
*/

public class GCD {
    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, (a % b));
    }
    public static void main(String[] args) {
        System.out.println(gcd(48, 12));
    }
}
