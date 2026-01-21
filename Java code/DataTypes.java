public class DataTypes {
  public static void main(String[] args) {
    String items[] = {"chicken", "eggs", "bananas", "water", "cookies"};
    int totalItems = items.length;
    int costPerItem = 12;
    char currency = '$';
    int priceOfItems = (totalItems * costPerItem);
    System.out.print("Price: " + currency + priceOfItems);
  }
}