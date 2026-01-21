import java.util.Scanner;

public class MathMethods {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter number 1:");
    String input1 = scanner.nextLine();
    int number1 = Integer.parseInt(input1);
    int rolledNumber = (int) (((Math.random() * 5) + 1));

    if (rolledNumber == 1) {

    int result = (number1 * number1);
    System.out.println("Square: " + result);

    } else if (rolledNumber == 2) {

    double result = Math.sqrt(number1);
    System.out.println("Square root: " + result);

    } else if (rolledNumber == 3) {

    System.out.println("Enter number 2 for max:");
    String input2 = scanner.nextLine();
    int number2 = Integer.parseInt(input2);
    int result = Math.max(number1, number2);
    System.out.println("Max: " + result);

    } else if (rolledNumber == 4) {

    System.out.println("Enter number 2 for min:");
    String input2 = scanner.nextLine();
    int number2 = Integer.parseInt(input2);
    int result = Math.min(number1, number2);
    System.out.println("Max: " + result);

    } else if (rolledNumber == 5) {

    int result = Math.abs(number1);
    System.out.println("Absolute Value: " + result);

    } else if (rolledNumber == 6) {

    System.out.println("Enter power:");
    String input2 = scanner.nextLine();
    int number2 = Integer.parseInt(input2);
    int result = (int) Math.pow(number1, number2);
    System.out.println("Result: " + result);

    }
  }
}