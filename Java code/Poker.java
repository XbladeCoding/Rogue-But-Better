/** *
 * Write a program Deal.java that takes an integer command-line argument n and prints n poker hands (five cards each) from a 
 * shuffled deck, separated by blank lines.
 * Author: Rohan Balasubramanian
 * Language: Java 24.0.2
 * DOC: 11/20/2025
 */
public class Poker {

    public static void main(String[] args) {
        int numberOfHands = Integer.parseInt(args[0]);
        String[] suits = {"Hearts", "Spades", "Clubs", "Diamonds"};
        String[] faces = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        String[] cards = new String[52];

        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < faces.length; x++) {
                cards[(i * 13) + x] = (faces[x] + " of " + suits[i]);
            }
        }
        for (int i = 0; i < numberOfHands; i++) {
            System.err.println("Hand " + (i + 1) + ":");
            for (int x = 0; x < 5; x++) {
                int randomCard = drawCard(cards);
                System.out.println(cards[randomCard]);
                cards[randomCard] = null;
            }
        }
    }

    private static int drawCard(String[] cards) {
        int randomCard = (int) (Math.random() * 51);
        if (cards[randomCard] == null) {
            drawCard(cards);
        }
        return randomCard;
    }
}
