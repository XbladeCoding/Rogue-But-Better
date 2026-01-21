public class DiscreteDistribution {
    public static void main(String[] args) {
        int[] probs = new int[args.length];
        int prob;
        for (int i = 0; i < args.length; i++) {
            prob = (Integer.parseInt(args[i]));
            probs[i] = prob;
        }
        int totalWeight = 0;
        for (int x = 0; x < probs.length; x++) {
            int weight = probs[x];
            totalWeight += weight;
        }
        int randNum = (int) (Math.random() * totalWeight);
        int sum = 0;
        for (int k = 0; k < probs.length; k++) {
            sum += probs[k];
            if (sum > randNum) {
                System.out.println(k);
                break;
            }
        }
    }
}
