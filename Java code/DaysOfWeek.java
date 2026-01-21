import java.util.Scanner;

public class DaysOfWeek {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter year:");
        int year = scanner.nextInt();
        String[] months = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        System.out.println("Enter month:");
        String monthName = scanner.next();
        int month = 0;
        for (int i = 0; i <= months.length; i++) {
            if (monthName.toLowerCase().equals(months[i + 1])) {
                month = i + 1;
            } else {
                break;
            }
        }
        System.out.println("Enter day:");
        int day = scanner.nextInt();

        
        int yzero = year - (14-month);
        int x = yzero + (yzero/4) + (yzero/100) + (yzero/400);
        int monthzero = month + 12*((14-month)/12) - 2;
        int dayzero = ((day + x + 31*monthzero / 12) % 7);
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        System.out.println("Day: " + days[dayzero]);
    }
}
