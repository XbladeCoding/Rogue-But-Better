public class Transition1D {
    public static void main(String[] args) {
        int n = StdIn.readInt();

        // Flatten 2D into 1D: counts[i][j] → counts[i*n + j]
        int[] counts = new int[n * n];
        int[] outDegree = new int[n];

        while (!StdIn.isEmpty()) {
            int i = StdIn.readInt();
            int j = StdIn.readInt();
            outDegree[i]++;
            counts[i * n + j]++; // Key transformation
        }

        StdOut.println(n + " " + n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Access pattern: row i, column j
                double p = 0.90 * counts[i * n + j] / outDegree[i] + 0.10 / n;
                StdOut.printf("%7.5f ", p);
            }
        StdOut.println();
        }
    }
}
