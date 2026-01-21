
import java.util.Scanner;

/*
 * In the Wages program, if an employee works over 40 hours in a week, the payment amount includes the overtime hours.
 *  An i f - e l s e statement is used to determine whether the number of hours entered by the user is greater than 40. 
 * If it is, the extra hours are paid at a rate one and a half times the normal rate. 
 * If there are no overtime hours, the total payment is based simply on the number of hours worked and the standard rate.
 * Author's name: Rohan Balasubramanian
 * JDK Version: 24.0.2
 * Date of completion: 10/10/2025
 */

public class Wages {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter hours worked: ");
        int hours = scanner.nextInt();
        System.out.println("Enter hourly wage: ");
        int wage = scanner.nextInt();
        int extraHours = 0;
        if (hours > 40) extraHours = (hours-40);
        double pay = wage*(hours-extraHours) + wage*(1.5*extraHours);
        System.out.println("Pay: " + pay + "$");
    }
}
