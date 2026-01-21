/*
Combination Generation: Write a recursive function to generate all combinations of a given set of elements.

This program takes 4 parameters: a list of String elements (elements), an empty String array (temp), and two other ints, index and size, which are both 0.
The extra paremeters help the code function and make the reccursive program work.

Author: Rohan Balasubramanian
Language: Java 24.0.1
DOC: 12/21/2025
*/

public class Combination {
    public static void combinations(String[] elements, String[] temp, int index, int size) {
        if (index == elements.length) {
            if (size > 0) {
                System.out.print("[");
                for (int i = 0; i < size; i++) {
                    System.out.print(temp[i]);
                    if (i < size -1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("]");
            }
            return;
        }
        temp[size] = elements[index];
        
        combinations(elements, temp, index + 1, size + 1);
        combinations(elements, temp, index + 1, size);
    }
    public static void main(String[] args) {
        String[] example = {"Cu", "Ar", "Ag", "U"};
        String[] nonpermanent = new String[example.length];
        combinations(example, nonpermanent, 0, 0);
    }
}
