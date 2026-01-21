public class CouponCollector {
    public static void main(String[] args) {
        int sizeOfCards = Integer.parseInt(args[0]);
        boolean[] collected = new boolean[sizeOfCards];
        for (int i = 0; i < sizeOfCards; i++) {
            collected[i] = false;
        }
        int distinctCollected = 0;
        int count = 0;
        int choice;
        while (distinctCollected != sizeOfCards) {
            choice = (int) (Math.random() * sizeOfCards);
            if (collected[choice]) {
                count++;
            } else {
                count++;
                collected[choice] = true;
                distinctCollected++;
            }
        }
        System.out.println(count);
    }
}
