/******************************************************************************

* Compilation: javac Euclid.java

* Execution: java Euclid p q *

* Reads two command-line arguments p and q and computes the greatest

* common divisor of p and q using Euclid's algorithm. *

* Remarks *
----------

 * - may return the negative of the gcd if either p or q is negative 
* ******************************************************************************/

public class EuclidAlg {
    public static int gcd(int p, int q) {
        if (q == 0) {
            return p;
        }
        return gcd(q, (p % q));
    }
    public static void main(String[] args) {
        System.out.println(gcd(102, 68) + " ");
        System.out.println(gcd(1440, 408) + " ");
        System.out.println(gcd(gcd(3, 6),  gcd(9, 12)) + " ");
    }
}
