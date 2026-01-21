public class HarmonicNumbers {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        double sum = 0.0;
        for (int x = 1; x <= (n); x++) {
            sum = sum + (1.0/x);
        }
        StdOut.println("Sum of harmonic numbers from x = 1 to x = " + n + ": " + sum);
    }
}
