/****************************************************************************** *

Compilation: javac GoldenRatio.java

* Execution: java GoldenRatio n *

* Computes an approximation to the golden ratio using the recursive

* formula f(0) = 1, f(n) = 1 + 1 / f(n-1) if n > 0. *

* % java GoldenRatio 5 *
1.625 *

* % java GoldenRatio 10
1.6179775280898876 *

* % java GoldenRatio 20
1.618033985017358 *

* % java GoldenRatio 30
1.6180339887496482

* ******************************************************************************/

public class GoldenRatio {
    public static double golden(int n) {
        if (n == 0) {
            return 1;
        }
        return 1 + 1 / golden(n-1);
    }
    public static void main(String[] args) {
        StdOut.println(golden(5) + " ");
    }
}
