/*
Write a program, ArrayBasics_YI.java to do the following:

1. Create an array a[] size 10. Assign a random integer number between 1 and 30 to each element.  

Display the first element of the array.

Display the last element of the array assuming you do not know the size of the array.

Display the messages given when you try to access an element with a negative index and with an index beyond the size of the array.

2. Create two arrays, suit[] and face_value[].

The suit[] array contains the 4 different suits as elements.

The face_value array contains “2”, “3”,…,”Jack”,…,”Ace” elements.

Use the random function to select five cards and display them.

3. Create two arrays x[] and y[] with N number of elements both.

Prompt the user for N.

Assign random integers to each element of the two arrays.

Then have another loop to find the product of each element with the same index and add them all together.

This operation is called the “dot product”:

Example: x = {2,3,4} and y = {5,6,7}  dotProd = 2x5 + 3x6 + 4x7 

Author: Rohan Balasubramanian
DOC: 2/3/2026
Language: Java 24.0.2
*/

public class ArrayBasicsRB {

    // PROBLEM 1
    private final int[] a = new int[10];

    public void createArray() {
        for (int i = 0; i < a.length-1; i++) {
            a[i] = ((int) (Math.random() * 30) + 1);
        }
    }

    public int getFirstElement() {
        return a[0];
    }

    public int getLastElement() {
        return a[a.length - 1];
    }

    public void ErrorMessages(int index) {
        try {
            System.out.println(a[index]);
        } catch (Exception e) {
            System.out.println("An error happened: " + e.getMessage());
        }
    }
    
    // PROBLEM 2

    private final String[] suits = {"Hearts", "Spades", "Diamonds", "Clubs"};
    private final String[] faces = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private final String[] cards = new String[5];

    public void getRandomCards() {
        System.out.println(cards.length);
        for (int i = 0; i < cards.length; i++) {
            String suit = suits[(int) (Math.random() * 4)];
            String face = faces[(int) (Math.random() * 13)];
            cards[i] = face + " of " + suit;
        }
        for (int x = 0; x < cards.length; x++) {
            System.out.println("Card " + x + ": " + cards[x]);
        }
    }

    // PROBLEM #3
    public int dotProduct(int n) {
        int[] x = new int[n];
        int[] y = new int[n];
        int dp = 0;
        
        for (int i = 0; i < x.length; i++) {
            x[i] = (int) (Math.random() * 10 + 1);
        }

        for (int i = 0; i < y.length; i++) {
            y[i] = (int) (Math.random() * 10 + 1);
        }

        for (int i = 0; i < n; i++) {
            int product = x[i] * y[i];
            dp += product;
        }

        return dp;
    }
}