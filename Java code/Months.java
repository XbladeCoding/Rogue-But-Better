/*
 * Create a code that prints the month name give the number of the month.
 * Author: Rohan Balasubramanian
 * Language: Java 24.0.2
 * DOC: 11/12/2025
*/


public class Months {
    public static void main(String[] args) {
        int monthNum = Integer.parseInt(args[0]);
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        System.out.println(months[monthNum - 1]);
    }
}

/*
 * Test 1:
 * Input: None
 * Output: Error (OOB Index value)
 * 
 * Test 2:
 * Input: 4
 * Output: "Jun" (non-saved code)
 * 
 * Test 3:
 * Input: 4
 * Output: "Apr"
*/