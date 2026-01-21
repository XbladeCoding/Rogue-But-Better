
import java.util.Arrays;

public class PrimeCounter {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        boolean[] primeOrNot = new boolean[n+1];
        Arrays.fill(primeOrNot, true);
        for (int i = 2; i*i <= n; i++) {
            if (primeOrNot[i] == true){
                for (int multiple = i*i; multiple <= n; multiple += i) {
                    primeOrNot[multiple] = false;
                }
            }
        }
        int count = 0;
        for (int i = 2; i < primeOrNot.length; i++) {
            if (primeOrNot[i] == true) {
                count++;
                System.out.println(i);
            }
        }
        System.out.println("Number of primes: " + count);
    }
}
