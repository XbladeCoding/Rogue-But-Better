/*
 * Create a method twoSum(nums, target) that takes an array of integers nums and an integer target and returns an array with 
 * the indices of two numbers such that they add up to target. If no two numbers add up to target, it returns an empty array. 
 * Assume that each input has exactly one solution, and you may not use the same element twice.
 * 
 * Author: Rohan Balasubramanian
 * Language: Java 24.0.2
 * DOC: 11/25/2025
*/

import java.util.Arrays;

public class SumArray {
    public static int[] sumInts(int[] nums, int target) {
        int index1 = 0;
        int index2 = 0;
        int[] indices = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int x = 0; x < nums.length; x++) {
                if (nums[i] == nums[x]) {
                    continue;
                } else if (nums[i] + nums[x] == target) {
                    index1 = i;
                    index2 = x;
                }
            }
        }
        indices[0] = index1;
        indices[1] = index2;
        return indices;
    }
    public static void main(String[] args) {
        int[] nums = {5, 9, 2, 7};
        System.out.println(Arrays.toString(sumInts(nums, 9)));
    }
}
