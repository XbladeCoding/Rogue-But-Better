/**
 * Code for branching
 * This code gets a integer from user
 * It prints positive integers until the counter reaches the user input
 * When the counter is same as user input, breaks the loop
 */

import java.util.Scanner;

public class Branching {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a positive integer: ");
        int userInput = scanner.nextInt();
        int counter = 1;
	System.out.println("Before Loop");
        while (true) {
            if (counter == userInput) {
                break;
            } 
            System.out.println(counter);
            counter++;
        }
	System.out.println("After Loop");
    }
}
