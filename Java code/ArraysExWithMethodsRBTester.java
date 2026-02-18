/*
Design and implement an application that creates a histogram that allows you to visually inspect the frequency distribution of 
a set of values. The program should read in an arbitrary number of integers that are in the range 1 to 100 inclusive; then 
produce a chart similar to the one below that indicates how many input values fell in the range 1 to 10, 11 to 20, and so on. 
Print one asterisk for each value entered.

1 -10    |  *****
11-20   |  **
21 - 30 |*******************
31-40   |
41-50   |.***
51 - 60 |********
61-70.  |**
71-80   |*****
81 - 90 | *******
91 - 100 | *********

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 2/14/2026
*/

public class ArraysExWithMethodsRBTester {
    public static void main(String[] args) {
        ArraysExWithMethodsRB tester = new ArraysExWithMethodsRB();
        tester.printFrequencies();
    }
}

class ArraysExWithMethodsRB {
    private final int[] frequencies = new int[10];
    private final int[] nums = new int[50];

    public void printFrequencies() {
        for (int i = 0; i < frequencies.length; i++) {
            frequencies[i] = 0;
        }

        for (int i = 0; i < nums.length; i++) {
            nums[i] = (int) (Math.random()*100 + 1);
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= 10 && nums[i] >= 1) {
                frequencies[0]++;
            } else if (nums[i] <= 20 && nums[i] >= 11) {
                frequencies[1]++;
            } else if (nums[i] <= 30 && nums[i] >= 21) {
                frequencies[2]++;
            } else if (nums[i] <= 40 && nums[i] >= 31) {
                frequencies[3]++;
            } else if (nums[i] <= 50 && nums[i] >= 41) {
                frequencies[4]++;
            } else if (nums[i] <= 60 && nums[i] >= 51) {
                frequencies[5]++;
            } else if (nums[i] <= 70 && nums[i] >= 61) {
                frequencies[6]++;
            } else if (nums[i] <= 80 && nums[i] >= 71) {
                frequencies[7]++;
            } else if (nums[i] <= 90 && nums[i] >= 81) {
                frequencies[8]++;
            } else if (nums[i] <= 100 && nums[i] >= 91) {
                frequencies[9]++;
            }
        }

        System.out.println("---------------HISTOGRAM---------------");
        for (int i = 0; i < frequencies.length; i++) {
            System.out.print((i*10 + 1) + " to " + (i*10 + 10) + ": ");
            for (int x = 0; x < frequencies[i]; x++) {
                System.out.print("* ");
            }
            System.out.println("");
        }
    }
}