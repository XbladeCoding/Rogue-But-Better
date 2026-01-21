public class IfElseLadder {
  public static void main(String[] args) {

    int numericGrade = Integer.parseInt(args[0]);

    if (numericGrade > 92) {
      System.out.print("Grade: A+");
    } else if (numericGrade > 90) {
      System.out.print("Grade: A");
    } else if (numericGrade > 80) {
      System.out.print("Grade: B");
    } else if (numericGrade > 70) {
      System.out.print("Grade: C");
    } else if (numericGrade > 60) {
      System.out.print("Grade: D");
    }
  }
}