public class DogYears {
  public static void main(String[] args) {
    int currentYear = 2025;
    int birthYear = 2010;
    int dogBirthYear = 2017;
    int age = currentYear - birthYear;
    int dogAge = currentYear - dogBirthYear;
    int dogYearsAge = dogAge * 7;
    System.out.println("Your age: " + age);
    System.out.println("Your dog age: " + dogAge);
    System.out.println("Your age in dog years: " + dogYearsAge);
  }
}