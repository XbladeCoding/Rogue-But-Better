
import java.util.Scanner;

public class Fibonacci {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter length of series: ");
        int n = scanner.nextInt();
        System.out.println("Series: ");
        int num1 = 0;
        int num2 = 1;
        System.out.println(num1);
        if (n==1) return;
        System.out.println(num2);
        for (int i = 2; i < n; i++) {
            int num = num1 + num2;
            System.out.println(num);
            num1 = num2;
            num2 = num;
        }
    }
}
