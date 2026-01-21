/*
Sum of Digits: Write a recursive function to find the sum of digits of a given number.

This code finds the sum of all the digits of n, or if n is a single digit, it prints n.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 12/16/2025
*/


public class SumDigitsProblem {
    public static int sumDigits(int n) {
        if (n < 10) {
            return n;
        }
        return (n % 10) + sumDigits(n / 10);
    }
    public static void main(String[] args) {
        System.out.println(sumDigits(67) + "");
    }
}
