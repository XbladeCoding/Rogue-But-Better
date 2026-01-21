import java.util.Scanner;

public class ScannerCode {
  public static void main(String[] args) {
    Scanner myObj = new Scanner(System.in);

    System.out.println("Write down your name, age, and 1st serve in percentage: ");

    String name = myObj.nextLine();
    int age = myObj.nextInt();
    double FstServeIn = myObj.nextDouble();

    System.out.println("Your name is " + name + ".");
    System.out.println("You are " + age + " years old.");
    System.out.println("You make " + FstServeIn + "% of your first serves in.");
  }
}