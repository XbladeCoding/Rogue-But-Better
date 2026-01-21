/*
 * Create a method sum13(nums) that takes an array of integers, nums and returns the sum of the numbers in the array. 
 * However, the number 13 is very unlucky, so do not add it or the number that comes immediately after a 13 to the sum. 
 * Return 0 if nums is an empty array.
 * Author: Rohan Balasubramanian
 * Language: Java 24.0.2
 * DOC: 11/21/2025
*/

public class Sum13Arrays {
    public static int sum13(int[] nums) {
        int sum = 0;

        for (int i = 0; i < nums.length; i++) {
            if (i == 0) {
                if (nums[i] != 13) {
                    sum += nums[i];
                }
            } else if (nums[i] != 13 && nums[i-1] != 13) {
                sum += nums[i];
            }
        }
        return sum;
    }
    public static void main(String[] args) {
        int[] numbers = {};
        System.out.println(sum13(numbers));
    }
}