/*
Palindrome Check: Implement a recursive function to check if a given string is a palindrome.

This code uses a recursive function to print whether a string is a palindrome or not.
It has three parameters: word (the word), lindex (left index, always 0), and rindex (length of word).

Author: Rohan Balasubramanian
Language: Java 24.0.2
DOC: 12/20/2025
*/

public class Palindrome {
    public static boolean checkPalindrome(String word, int lindex, int rindex) {
        if ((rindex - lindex) <= 0) {
            return true;
        }
        if ((word.indexOf(rindex)) != (word.indexOf(lindex))) {
            return false;
        }
        return checkPalindrome(word, lindex + 1, rindex - 1);
    }
    public static void main(String[] args) {
        System.out.println(checkPalindrome("racecar", 0, 7));
    }
}


/**
 * aedrirdea 
 * palidrome function(string, left index, right index)
 * right == left  return true;
 * str[0=left] == str (right] = palindrome(str, left+1, rigjt+1)
 * 
 * if same -> palindrome (str, left+1, right-1)
 * if differe3nt - return false
 * 
 */