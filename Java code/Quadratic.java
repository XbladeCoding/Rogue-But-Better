/**
 * Write a program, QuadraticEQ_YI.java that takes as input arguments the coefficients of the quadratic equation, a, b, c
 * and finds the solutions of the equation. In other words, if a, b and c are the coefficients of ax^2 + bx + c, then a 
 * can be obtained with args[0], b with args[1] and c with args[2] 
 * NOTE: Assume the equation has two solutions. Display your solutions in full sentence.
 * Author: Rohan Balasubramanian
 * Java 24.0.2
 * DOC: 10/26/25
 **/

public class Quadratic {
    public static void main(String[] args) {
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        int c = Integer.parseInt(args[2]);
        int discriminant = (int) Math.sqrt(Math.pow(b, 2) - (4*a*c));
        double root1 = (-b + discriminant)/(2*a);
        double root2 = (-b - discriminant)/(2*a);
        System.out.println("The roots of the solution are x = " + root1 + " and x = " + root2 + ".");
    }
}

/**
 * Case 1:
 * Inputs: 1, 4, 4
 * Outputs: -2.0 and -2.0
 * 
 * Case 2:
 * Inputs: 4, 0, -4
 * Outputs: 1.0 and -1.0
 * 
 * Case 3:
 * Inputs: 1, 1, 1
 * Outputs: 0.0 and 0.0
 **/