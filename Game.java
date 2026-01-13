
import java.util.Scanner;

public class Game {
    // return the site occupied by the monster
    // searches the dungeon for the monster's character symbol and returns its Site
    public Site getMonsterSite() {
        Dungeon dungeon = getDungeon();
        int n = dungeon.size();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Site currentSite = new Site(i, j);
                if (dungeon.isMonsterSite(currentSite)) {
                    return currentSite;
                }
            }
        }
        return null; // in case monster not found
    }
    // return the site occupied by the rogue
    // searches the dungeon for the rogue's character symbol and returns its Site
    public Site getRogueSite() {
        Dungeon dungeon = getDungeon();
        int n = dungeon.size();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Site currentSite = new Site(i, j);
                if (dungeon.isRogueSite(currentSite)) {
                    return currentSite;
                }
            }
        }
        return null; // in case rogue not found
    }
    // return the dungeon (n x n matrix which is the level at its current state)
    // example dungeon:
    /**
     * 10
    + + + + + + + +
    +             +
. . . . . . .     +
. . . . . . .     +
. . . . @ . .     +
. . B . . . .     +
. . . . . . .     +
. . . . . . . + + +
. . . . . . .      
. . . . . . .      
     * 
    */
    public Dungeon getDungeon() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine(); // consume the rest of the line
        char[][] userMatrix = new char[n][n];
        // read in the n x n matrix from user input
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            userMatrix[i] = line.toCharArray();
        }

        return new Dungeon(userMatrix);
    }
}