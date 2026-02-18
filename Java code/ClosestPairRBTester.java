/*
Write a program, ClosestPair_YI.java to create an array of 100 random integers between the values of 1 and 1000 and find the two consecutive integers with the smallest difference between the two of them.

Help: use the absolute value of the difference.

Display all the elements of the array.

Display the two values and the difference in a full sentence.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 2/9/25
*/

public class ClosestPairRBTester {
    public static void main(String[] args) {
        ClosestPairRB tester = new ClosestPairRB();
        tester.randomize();
        tester.display();
        int[] ans = tester.closetPair();
        System.out.print(" between " + ans[0] + " and " + ans[1] + "\n");
    }
}

class ClosestPairRB {
    private final int[] nums = new int[100];
    private int diff = -1;
    private final int[] ans = new int[2];
    
    public void randomize() {
        for (int i = 0; i < nums.length - 1; i++) {
            nums[i] = (int) (Math.random() * 1000 + 1);
        }
    }

    public void display() {
        for (int x = 0; x < nums.length - 1; x++) {
            System.out.println(nums[x]);
        }
    }

    public int[] closetPair() {
        for (int y = 0; y < nums.length - 1; y++) {
            if (y != 99) {
                if (diff == -1) {
                    ans[0] = nums[y];
                    ans[1] = nums[y + 1];
                    diff = Math.abs(nums[y] - nums[y + 1]);
                }
                if (diff > (Math.abs(nums[y] - nums[y + 1]))) {
                    ans[0] = nums[y];
                    ans[1] = nums[y + 1];
                    diff = Math.abs(nums[y] - nums[y + 1]);
                }
            } else {
                break;
            }
        }
        System.out.println("There is a difference of " + diff);
        return ans;
    }
}