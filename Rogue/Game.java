
import java.util.Scanner;

public class Game {
    private Dungeon dungeon;      // The dungeon grid
    private Site rogueSite;       // Current position of the rogue
    private Site monsterSite;     // Current position of the monster
    private char monsterChar;     // The monster's letter (A-Z)

    // Constructor: reads dungeon from Scanner and initializes game state
    public Game(Scanner scanner) {
        // Read the size
        int n = scanner.nextInt();
        scanner.nextLine(); // consume newline after number

        // Read the grid
        char[][] grid = new char[n][n];
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            // Pad line if it's shorter than n characters (spaces at end may be trimmed)
            while (line.length() < n) {
                line = line + " ";
            }
            // Take only first n characters if line is longer
            for (int j = 0; j < n; j++) {
                grid[i][j] = line.charAt(j);
            }
        }

        // Find rogue (@) and monster (uppercase letter A-Z)
        // Replace them with room tiles (.) in the grid
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char c = grid[i][j];
                if (c == '@') {
                    rogueSite = new Site(i, j);
                    grid[i][j] = '.';  // Rogue stands on a room tile
                } else if (c >= 'A' && c <= 'Z') {
                    monsterSite = new Site(i, j);
                    monsterChar = c;
                    grid[i][j] = '.';  // Monster stands on a room tile
                }
            }
        }

        // Create the dungeon with the cleaned grid
        this.dungeon = new Dungeon(grid);
    }

    // Return the site occupied by the rogue
    public Site getRogueSite() {
        return rogueSite;
    }

    // Return the site occupied by the monster
    public Site getMonsterSite() {
        return monsterSite;
    }

    // Return the dungeon
    public Dungeon getDungeon() {
        return dungeon;
    }

    // Get the monster's character (for display)
    public char getMonsterChar() {
        return monsterChar;
    }

    // Update rogue position (called after rogue moves)
    public void setRogueSite(Site newSite) {
        this.rogueSite = newSite;
    }

    // Update monster position (called after monster moves)
    public void setMonsterSite(Site newSite) {
        this.monsterSite = newSite;
    }

    // Display the current game state
    public void display() {
        int n = dungeon.size();
        System.out.println("\n=== Game State ===");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Site current = new Site(i, j);
                // Show rogue, monster, or the underlying tile
                if (current.equals(rogueSite)) {
                    System.out.print('@');
                } else if (current.equals(monsterSite)) {
                    System.out.print(monsterChar);
                } else {
                    System.out.print(dungeon.getChar(current));
                }
            }
            System.out.println();
        }
        System.out.println("Rogue: " + rogueSite + "  Monster: " + monsterSite);
    }

    // Check if the game is over (monster caught the rogue)
    public boolean isGameOver() {
        return rogueSite.equals(monsterSite);
    }

    // Main game loop - monster and rogue take turns
    // Returns the number of turns the rogue survived
    public int play(Monster monster, Rogue rogue, int maxTurns) {
        int turn = 0;

        System.out.println("=== GAME START ===");
        display();

        while (turn < maxTurns) {
            turn++;
            System.out.println("\n--- Turn " + turn + " ---");

            // MONSTER MOVES FIRST
            Site monsterMove = monster.move();
            // Validate the move is legal
            if (dungeon.isLegalMove(monsterSite, monsterMove)) {
                setMonsterSite(monsterMove);
                System.out.println("Monster moves to " + monsterMove);
            } else {
                System.out.println("Monster stays at " + monsterSite + " (invalid move attempted)");
            }

            // Check if monster caught rogue
            if (isGameOver()) {
                display();
                System.out.println("\n*** GAME OVER! Monster caught the rogue on turn " + turn + "! ***");
                return turn;
            }

            // ROGUE MOVES SECOND
            Site rogueMove = rogue.move();
            // Validate the move is legal
            if (dungeon.isLegalMove(rogueSite, rogueMove)) {
                setRogueSite(rogueMove);
                System.out.println("Rogue moves to " + rogueMove);
            } else {
                System.out.println("Rogue stays at " + rogueSite + " (invalid move attempted)");
            }

            // Check if monster caught rogue (rogue walked into monster)
            if (isGameOver()) {
                display();
                System.out.println("\n*** GAME OVER! Rogue walked into the monster on turn " + turn + "! ***");
                return turn;
            }

            display();
        }

        System.out.println("\n*** Rogue survived " + maxTurns + " turns! ***");
        return turn;
    }

    // ============================================================
    // PRE-DEFINED TEST DUNGEONS
    // ============================================================
    
    // Small 5x5 dungeon - simple room with corridor border
    public static final String DUNGEON_SMALL = 
        "5\n" +
        " +++ \n" +
        "+.@.+\n" +
        "+...+\n" +
        "+.M.+\n" +
        " +++ \n";

    // 7x7 dungeon - room with corridors, good for testing movement
    public static final String DUNGEON_CORRIDORS = 
        "7\n" +
        "  +++  \n" +
        " ++.++ \n" +
        "+..@..+\n" +
        "+.....+\n" +
        "+..M..+\n" +
        " ++.++ \n" +
        "  +++  \n";

    // 8x8 dungeon - two rooms connected by corridor (from assignment)
    // Rogue can potentially escape by running around the corridor
    public static final String DUNGEON_TWO_ROOMS = 
        "8\n" +
        " ++++++ \n" +
        "+......+\n" +
        "+.@....+\n" +
        "+......+\n" +
        "+......+\n" +
        "+....M.+\n" +
        "+......+\n" +
        " ++++++ \n";

    // 10x10 dungeon - L-shaped corridor, tests escape routes
    public static final String DUNGEON_L_CORRIDOR = 
        "10\n" +
        "          \n" +
        " ++++++++ \n" +
        " +......+ \n" +
        " +.@....+ \n" +
        " +......+ \n" +
        " +....+++ \n" +
        " +....+   \n" +
        " +..M.+   \n" +
        " ++++++   \n" +
        "          \n";

    // Tricky dungeon - monster can trap rogue in corner
    public static final String DUNGEON_TRAP = 
        "6\n" +
        "......\n" +
        "......\n" +
        "..@...\n" +
        "......\n" +
        "....A.\n" +
        "......\n";

    // Helper method to create game from pre-defined dungeon
    public static Game fromTestDungeon(String dungeonString) {
        return new Game(new Scanner(dungeonString));
    }

    // Helper to create game from user input (for later)
    public static Game fromUserInput() {
        System.out.println("Enter dungeon (first line: size N, then N lines of N characters):");
        return new Game(new Scanner(System.in));
    }

    public static void main(String[] args) {
        // ========== TEST THE GAME LOOP ==========
        System.out.println("========== TESTING GAME LOOP ==========\n");
        
        // Create a game with the small dungeon
        Game game = Game.fromTestDungeon(DUNGEON_SMALL);
        
        // Create monster and rogue (both use placeholder "stay in place" strategy)
        Monster monster = new Monster(game);
        Rogue rogue = new Rogue(game);
        
        // Run the game for up to 5 turns
        // Since both stay in place, the game should run all 5 turns
        int turns = game.play(monster, rogue, 5);
        
        System.out.println("\nGame ended after " + turns + " turns.");
        
        // ========== SHOW ALL DUNGEONS ==========
        System.out.println("\n\n========== AVAILABLE TEST DUNGEONS ==========");
        
        System.out.println("\n--- DUNGEON_SMALL ---");
        Game.fromTestDungeon(DUNGEON_SMALL).display();
        
        System.out.println("\n--- DUNGEON_CORRIDORS ---");
        Game.fromTestDungeon(DUNGEON_CORRIDORS).display();
        
        System.out.println("\n--- DUNGEON_TWO_ROOMS ---");
        Game.fromTestDungeon(DUNGEON_TWO_ROOMS).display();
        
        System.out.println("\n--- DUNGEON_L_CORRIDOR ---");
        Game.fromTestDungeon(DUNGEON_L_CORRIDOR).display();
        
        System.out.println("\n--- DUNGEON_TRAP ---");
        Game.fromTestDungeon(DUNGEON_TRAP).display();
        
        // Uncomment this line when you want to test user input:
        // Game userGame = Game.fromUserInput();
        // userGame.display();
    }
}