/*
Tower of Hanoi: Solve the Tower of Hanoi problem using recursion.

This program solves the Tower of Hanoi and takes three paramters.
n (the number of disks), source (char name of source tower), destination (char name of destination tower), and auxiliary (char name of auxiliary tower).

Author: Rohan Balasubramanian
Language: Java 24.0.1
DOC: 12/20/2025
*/

public class TowerHanoi {
    public static void hanoi(int n, char source, char destination, char auxiliary) {
        if (n == 1) {
            System.out.println("Move disk 1 from " + source + " to " + destination + ".");
            return;
        }

        hanoi(n - 1, source, auxiliary, destination);
        
        System.out.println("Move disk " + n + " from " + source + " to " + destination + ".");

        hanoi(n - 1, auxiliary, destination, source);
    }
    public static void main(String[] args) {
        hanoi(4, 'A', 'B', 'C');
    }
}
