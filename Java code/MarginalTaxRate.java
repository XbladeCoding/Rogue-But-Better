/*
 * MarginalTaxRate_YI.java computes the marginal tax rate for a given income and calculates 
 * the tax. It uses several nested if-else statements to test from among a number of 
 * mutually exclusive possibilities.
 * 
 * Author: Rohan Balasubramanian
 * DOC: 10/30/2025
 * Language: Java 24.0.2
*/

public class MarginalTaxRate {
    public static void main(String[] args) {
        int income = Integer.parseInt(args[0]);
        int taxRate = 0;
        if (income > 609351) {
            taxRate += 37;
        } else if (income > 243726 && income < 609351) {
            taxRate += 35;
        } else if (income > 191951 && income < 243725) {
            taxRate += 32;
        } else if (income > 100526 && income < 191950) {
            taxRate += 24;
        } else if (income > 47151 && income < 100525) {
            taxRate += 22;
        } else if (income > 11601 && income < 47150) {
            taxRate += 12;
        } else if (income > 0 && income < 11600) {
            taxRate += 10;
        } else {
            StdOut.println("Sorry, invalid input.");
        }
        StdOut.println("Income tax: " + taxRate + "%");
    }
}

/* 
 * First run:
 * Inputs: 14610
 * Outputs: None (no print statement)
 * 
 * Second run:
 * Inputs: 14610
 * Outputs: "Income tax: 12" (no percent)
 * 
 * Third run:
 * Inputs: 14610
 * Outputs: "Income tax: 12%"
*/