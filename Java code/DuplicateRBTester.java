/*
Write a program, Duplicate_YI.java to find a duplicate. Given an array of N elements with each element between 1 and N, write an algorithm to determine whether there are any duplicates. You do not need to preserve the contents of the given array, but do not use an extra array.

Create an array with N = 50 random integer numbers between 1 and 50 inclusive.
Details are given:

Create an array with 50 random integer numbers between 1 and 50 inclusive.
Print the array in 10 rows by 5 columns. Use “printf” to keep the columns aligned.
Print all the duplicate values and their index. Format:
value 1, index n1, index n2 … per line.
value 2, index n1, index n2 … per line.
value 3, index n1, index n2 … per line.
Print the total number of duplicates as the last message in your output.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 2/9/2026
*/

public class DuplicateRBTester {
    public static void main(String[] args) {
        DuplicateRB tester = new DuplicateRB();
        tester.findDuplicates();
    }
}

class DuplicateRB {

    private Integer val1;
    private Integer val2;
    private final Integer[] nums = new Integer[50];

    public void findDuplicates() {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            nums[i] = (int) (Math.random() * 50 + 1);

        }
        for (int i=0;i<nums.length;i++){
            System.out.printf("%3s", nums[i]);
            if (i!=0 && (i+1)%5==0){
                System.err.println(" ");
            }
        }
        for (int i = 0; i < nums.length - 1; i++) {
            int counter = 0;
            for (int j = 0; j < nums.length; j++) {
                val1 = nums[i];
                val2 = nums[j];

                if (val1!=null && val2!= null && (val1.equals(val2)) && (i != j)) {
                    counter++;
                    if (counter == 1) {
                        System.out.print("Value " + nums[i] + ", Index " + i + ", index " + j);
                        count++;
                    } else {
                        if (counter>1){
                            System.out.print(", index " + j);
                        }
                    }
                    nums[j]=null;
                }
            }
            if (counter>=1){
                System.out.println(" ");
            }
        }
    }
}

// NOTE: This code was written on the assumption that the 'number of duplicates' is number
// of unique duplicate values, not of how many values are duplicates.