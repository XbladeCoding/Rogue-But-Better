/*
Write a program, AverageArray_YI.java to create an array of 100 random integers between the values of 1 and 1000 and display 
the average of all the elements of the array.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 2/9/2026
*/

public class AverageArrayRBTester {
    public static void main(String[] args) {
        AverageArrayRB tester = new AverageArrayRB();
        System.out.println(tester.findAvg());
    }
}

class AverageArrayRB {
    int[] nums = new int[100];
    private int sum = 0;

    public int findAvg() {
        for (int i = 0; i < nums.length - 1; i++) {
            nums[i] = (int) (Math.random() * 1000 + 1);
        }

        for (int i = 0; i < nums.length - 1; i++) {
            sum += nums[i];
        }

        return sum / nums.length;
    }
}