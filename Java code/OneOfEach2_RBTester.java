/*
OneOfEach2_YI.java: implement this program to allow the user an additional input, the specific number of trials. 
Each trial will calculate how many cards (no_of_cards) were drawn until all different cards were drawn. 
End the program by displaying the average number of cards needed for all the M cards to come up. 
Again, assuming there is an infinite number of cards.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 2/16/2026
*/

public class OneOfEach2_RBTester {
    public static void main(String[] args) {
        OneOfEach2_RB tester = new OneOfEach2_RB();
        // System.out.println(tester.collect(13));
        System.out.println("" + tester.collectAvg(10, 5));
    }
}

class OneOfEach2_RB {
    
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

    public int collectAvg(int faces, int trials) {
        int sum = 0;
        for (int i = 0; i < trials; i++) {
            sum += collect(faces);
        }
        return sum / trials;
    }
}

/*
How many different cards are in your deck? 8
How many trials? 20000
In 20,000 trials, on average 31 cards were randomly drawn to acquire the full collection.

 

How many different cards are in your deck? 13
How many trials? 100000
In 100,000 trials, on average 52 cards were randomly drawn to acquire the full collection.

 

How many different cards are in your deck? 10
How many trials? 5
In 5 trials, on average 33 cards were randomly drawn to acquire the full collection.
*/