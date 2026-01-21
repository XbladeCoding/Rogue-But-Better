/*
Finish the following method (getSumNonNeg(int[] arr)) to return the sum 
of all of the non-negative values in the passed array.
Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 12/9/2025

--------------------------------------------------------
Test 1:
Input: None
Output: The code should print 11 and your answer is: 11
--------------------------------------------------------
*/

public class MiniQuiz {
    public static int getSumNonNeg(int[] arr) {
        int[] nonnegs = new int[arr.length];
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 0) {
                nonnegs[i] = arr[i];
            }
        }
        for (int x = 0; x < nonnegs.length; x++) {
            sum += nonnegs[x];
        }
        return sum;
    }
    public static void main(String[] args)
    {
        int[] a1 = {1, 2, 5, 3, -1, -20};
        System.out.println(
                "The code should print 11 "
                        + "and your answer is: "
                        + getSumNonNeg(a1));
    }
}
