public class TypeCasting {
  public static void main(String[] args) {
    int maxScore = 500;
    int userScore = 423;
    float max = (float) maxScore;
    float user = (float) userScore;
    float percentage = (user/max)*100;
    System.out.println("Completion: " + percentage);
  }
}