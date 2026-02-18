import java.util.Scanner;

public class ArrayBasicsTester {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayBasicsRB arrayTester = new ArrayBasicsRB();

        System.out.println("=========== PROBLEM 1 ===========");
        // Creates the array
        arrayTester.createArray();

        // Prints the first element in the array
        System.out.println("First element: " + arrayTester.getFirstElement());
        
        // Prints the last element in the array
        System.out.println("Last element: " + arrayTester.getLastElement());

        // Prints error messages in two circumstances

        // if index is out of bounds
        System.out.println("OOB Index:");
        arrayTester.ErrorMessages(16);
        // if index is negative
        System.out.println("Negative Index:");
        arrayTester.ErrorMessages(-4);

        System.out.println("=========== PROBLEM 2 ===========");
        arrayTester.getRandomCards();

        System.out.println("=========== PROBLEM 3 ===========");
        System.out.println("Enter a number: ");
        int n = scanner.nextInt();
        System.out.println("Dot product: " + arrayTester.dotProduct(n));
    }
}
