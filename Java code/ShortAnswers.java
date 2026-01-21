public class ShortAnswers {
    private void printAnswer(double x, double y, double z) {
        x++;
        y++;
        z++;
    }

    private String welcomeMsg(String name, int vnum) {
        String msg = ("Welcome " + name + "! You are visitor number " + vnum + ".");
        return msg;
    }

    private void powersOfTwo() {
        int x = 2;
        System.out.println(x);
        for (int counter = 1; counter < 10; counter++) {
            x = x * 2;
            System.out.println(x);
        }
    }

    private void alarm(int times) {
        if (times < 1) {
            System.out.println("ERROR: Enter a number more than 1.");
        }
        for (int i = 0; i < times; i++) {
            System.out.println("Alarm! ");
        }
    }

    private int sum100() {
        int sum = 0;
        for (int x = 1; x <= 100; x++){
            sum = sum + x;
        }
        return sum;
    }

    private int maxOfTwo(int f, int g) {
        return Math.max(f, g);
    }

    private int sumRange(int num1, int num2) {
        int sumInts = 0;
        for (int y = num1; y <= num2; y++) {
            sumInts = sumInts + y;
        }
        return sumInts;
    }
    public static void main(String[] args) {
        ShortAnswers shortAnswers = new ShortAnswers();
        shortAnswers.welcomeMsg("Rohan", 6);
        shortAnswers.printAnswer(0.1, 0.2, 0.3);
        shortAnswers.powersOfTwo();
        shortAnswers.alarm(6);
        System.out.println(shortAnswers.sum100());
        System.out.println(shortAnswers.maxOfTwo(10, 6) + "");
        System.out.println(shortAnswers.sumRange(40, 70) + "");
    }
}
