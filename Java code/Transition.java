public class Transition {


    public static void main(String[] args) {

        int n = StdIn.readInt();           // number of pages
        int[][] counts = new int[n][n];    // counts[i][j] = # links from page i to page j
        int[] outDegree = new int[n];      // outDegree[i] = # links from page i to anywhere

        // Accumulate link counts.
        while (!StdIn.isEmpty())  {
            int i = StdIn.readInt();
            int j = StdIn.readInt();
            outDegree[i]++;
            counts[i][j]++;
        }
        StdOut.println(n + " " + n);


        // Print probability distribution for row i.
        for (int i = 0; i < n; i++)  {

            // Print probability for column j.
            for (int j = 0; j < n; j++) {
                double p = 0.90*counts[i][j]/outDegree[i] + 0.10/n;
                StdOut.printf("%7.5f ", p);
            }
            StdOut.println();
        }
    }
}
