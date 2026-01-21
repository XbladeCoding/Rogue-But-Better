import java.util.Scanner;

public class LoopInd {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a year: ");
        int year = Integer.parseInt(scanner.nextLine());
        if (((year % 400) == 0) || ((year % 400) == 0)) {
            System.out.println("It's a leap year!");
        }
        if (((year % 100) == 0) || ((year % 4) != 0)) {
            System.out.println("It's not a leap year!");
        }
    }
}
