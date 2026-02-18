/*
Write a program, MaxInArray_YI.java to create an array of 100 
random integers between the values of 1 and 1000 and display the largest value.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 2/9/2026
*/

public class MaxInArrayRBTester {
    public static void main(String[] args) {
        MaxInArrayRB tester = new MaxInArrayRB();
        System.out.println(tester.findMax());
    }
}

class MaxInArrayRB {
    int[] a = new int[100];
    private int val = Integer.MAX_VALUE;

    public int findMax() {
        for (int i = 0; i < a.length; i++) {
            a[i] = (int) (Math.random() * 1000 + 1);
        }

        for (int i = 0; i < a.length - 1; i++) {
            val = a[i];
        }
        return val;
    }
}
