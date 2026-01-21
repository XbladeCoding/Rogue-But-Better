/*
Binary Search: Implement a recursive binary search algorithm for a sorted array.

This code takes an array and a target, and uses a binary search method to determine the index of the target value.
It says 'TARGET NOT FOUND' if the target is not in the array.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 12/16/2025
*/


public class BinarySearch {
    public static int search(int[] nums, int target, int left_bound, int right_bound) {
        int mid = left_bound + ((right_bound - left_bound)/2);
        if (left_bound > right_bound || left_bound == right_bound) {
            System.out.println("TARGET NOT FOUND");
            return -1;
        }
        if (nums[mid] == target) {
            return mid;
        }
        if (nums[mid] < target) {
            return search(nums, target, mid + 1, right_bound);
        }
        return search(nums, target, left_bound, mid - 1);
    }
    public static void main(String[] args) {
        int[] integews = {1,2,3,4,5,6,7,8,9};
        System.out.println(search(integews, 6, 0, integews.length));
        System.out.println(search(integews, 9, 0, integews.length));
        System.out.println(search(integews, 100, 0, integews.length));
    }
}
