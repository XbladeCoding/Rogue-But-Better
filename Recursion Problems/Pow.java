/*
Power Function: Implement a recursive function to calculate the power of a number (x^n).

This code prints an integer that is x^n, where x is the first number and the base, and n is the secong number and the power.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 12/16/2025
*/

public class Pow {
    public static int pow(int base, int power) {
        if (power == 1) {
            return base;
        }
        return base * pow(base, power-1);
    }
    public static void main(String[] args) {
        System.out.println(pow(3,3));
    }
}
