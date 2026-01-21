public class Variables {
  public static void main(String[] args) {
    int length = Integer.valueOf(args[0]);
    int width = Integer.valueOf(args[1]);
    final int area = (length * width);
    System.out.println("Area: " + area);
  }
}