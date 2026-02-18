/*
A school has 100 lockers and 100 students. All lockers are closed on the first day of school. As the students enter, the first 
student, denoted S1, opens every locker. Then the second student, S2, begins with the second locker, denoted L2, and closes 
every other locker. Student S3 begins with the third locker and changes every third locker (closes it if it was open, and opens
it if it was closed). Student S4 begins with locker L4 and changes every fourth locker. Student S5 starts with L5 and changes 
every fifth locker, and so on until student S100 changes L100.
After all the students have passed through the building and changed the lockers, which lockers are open?

 

Write a program, WhichLockers_YI.java to find your answer and display all open locker numbers separated by exactly one space.

Hint: Use an array of 100 Boolean elements, each of which indicates whether a locker is open (true) or closed (false). 
Initially, all lockers are closed.

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 2/10/2026
*/

import java.util.Arrays;

public class WhichLockersRBTester {
    public static void main(String[] args) {
        WhichLockersRB tester = new WhichLockersRB();
        tester.findLockers();
    }
}

class WhichLockersRB {
    private final boolean[] lockers = new boolean[100];
    
    public void findLockers() {
        Arrays.fill(lockers, false);

        for (int student = 0; student < lockers.length; student++) {
            for (int locker = student; locker < lockers.length; locker++) {
                if ((locker + 1) % (student + 1) == 0) {
                    if (lockers[locker] == true) {
                        lockers[locker] = false;
                    } else if (lockers[locker] == false) {
                        lockers[locker] = true;
                    }
                }
            }
        }

        for (int i = 0; i < lockers.length; i++) {
            if (lockers[i] == true) {
                System.out.print((i + 1) + " ");
            }
        }
    }
}