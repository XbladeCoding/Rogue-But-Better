import java.util.Scanner;

public class RockPaperScissors {
    public static void main(String[] args) {
        while (1 > 0) {
            Scanner scanner = new Scanner(System.in);
            int wins = 0;
            int losses = 0;
            int ties = 0;
            String choices[] = {"rock", "paper", "scissors"};
            String cpuChoice = choices[(int) (Math.random() * 3)];
            String yourChoice = (scanner.nextLine()).toLowerCase();
            System.out.println("Computer chose: " + cpuChoice);

            if (yourChoice.equals(cpuChoice)) {
                System.out.println("Tie!");
                ties++;
            } else if ((yourChoice.equals("rock") && cpuChoice.equals("scissors")) || (yourChoice.equals("paper") && cpuChoice.equals("rock")) || (yourChoice.equals("scissors") && cpuChoice.equals("paper"))) {
                System.out.println("You win!");
                wins++;
            } else {
                System.out.println("You lose!");
                losses++;
            }

            System.out.println("W-T-L Record: " + wins + "-" + ties + "-" + losses);
        }
    }
}