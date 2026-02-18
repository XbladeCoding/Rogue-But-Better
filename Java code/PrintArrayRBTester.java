/*
Write a program, PrintArray_YI.java to create an array of 100 random integers between the values of 1 and 1000 and 
print them in 5 columns separated by 3 spaces. Note: Use "printf" so the size of the number doesn't affect the alignment.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 2/9/2026
*/

public class PrintArrayRBTester {
    public static void main(String[] args) {
        PrintArrayRB tester = new PrintArrayRB();
        tester.printArray();
    }
}

class PrintArrayRB {
    int[] nums = new int[100];
    
    public void printArray() {
        for (int i = 0; i < nums.length - 1; i++) {
            nums[i] = (int) (Math.random() * 1000 - 1);
        }

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 5; j++) {
                if (String.valueOf(nums[j*20 + i]).length() == 3) {
                    System.out.print("   " + nums[j*20 + i]);
                } else if (String.valueOf(nums[j*20 + i]).length() == 2) {
                    System.out.print("    " + nums[j*20 + i]);
                } else if (String.valueOf(nums[j*20 + i]).length() == 1) {
                    System.out.print("     " + nums[j*20 + i]);
                }
                
            }
            System.out.println("");
        }
    }
}