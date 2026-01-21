/*
 * Using the Array Lesson, you will now proceed to write this simple java code.
 * 
 * 
 * This program takes two command-line arguments m and n and produces
 *  a random sample of m of the integers from 0 to n-1.
 *
 *  % java Sample 6 49
 *  10 20 0 46 40 6
 *
 *  % java Sample 10 1000
 *  656 488 298 534 811 97 813 156 424 109
 * 
 * Author: Rohan Balasubramanian
 * Language: Java 24.0.2
 * DOC: 11/12/2025
 * 
*/

public class SimpleArrays {
    public static void main(String[] args) {
        int m = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        int[] nums = new int[m];
        for (int i = 0; i < (m); i++) {
            int num = (int) (Math.random() * (n));
            nums[i] = num;
        }
        for (int x = 0; x < nums.length; x++) {
            System.out.print(nums[x] + " ");
        }
    }
}
/*
 * Test 1: 
 * Inputs: 8 12
 * Outputs: nonsense numbers that did not make sense
 * 
 * Test 2: 
 * Inputs: 8 12
 * Outputs: 9 3 11 8 1 6 4 2
 * 
 * I changed a single quote to a double quote, hence fixing the error that happened.
 * 
*/