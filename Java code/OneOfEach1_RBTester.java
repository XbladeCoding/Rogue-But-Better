/*
Suppose that you have a shuffled deck of cards and you turn them face up, one by one. How many cards do you need to turn up 
before you have seen one of each (face) value?
OneOfEach1_YI.java: this program will allow the user to pick as many cards as needed until all the (face) values in any deck 
of cards come up. Your program should generate the number of cards needed to find all the (face) values. The input should be 
M, the number of different (face) values.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 2/14/2026
*/

public class OneOfEach1_RBTester {
    public static void main(String[] args) {
        OneOfEach1_RB tester = new OneOfEach1_RB();
        System.out.println(tester.collect(13));
    }
}

class OneOfEach1_RB {
    
    public int collect(int faces) {
        int count = 0;
        int collected;
        for (int i = 0; i < faces; i++) {
            collected = (int) (Math.random() * 4 + 1);
            if (collected == 1) {
                count++;
            } else {
                count++;
                i--;
            }
        }
        return count;
    }
}

/*
How many different cards are in your deck? 13
After randomly drawing 37 cards, the full collection was acquired.

How many different cards are in your deck? 13
After randomly drawing 83 cards, the full collection was acquired.

How many different cards are in your deck? 13
After randomly drawing 39 cards, the full collection was acquired.

How many different cards are in your deck? 13
After randomly drawing 42 cards, the full collection was acquired.
*/