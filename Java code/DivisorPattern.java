public class DivisorPattern {
    public static void main(String[] args) {
        int times = Integer.parseInt(args[0]);
        for (int x = 1; x < (times + 1); x++) {
            for (int i = 1; i < (times + 1); i++) {
                if ((x % i == 0) | (i % x == 0)) {
                    StdOut.print(" * ");
                } else {
                    StdOut.print("   ");
                }
            }
            StdOut.print(x + "\n");
        }
    }
}
