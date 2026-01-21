import java.util.Scanner;

public class WhileSum {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int i = 1;
    int endNum = scanner.nextInt();
    int sum = 0;
    while (i < (endNum + 1)) {
      sum = sum + i;
      i++;
    }
    System.out.println("Sum: " + sum);
  }
}