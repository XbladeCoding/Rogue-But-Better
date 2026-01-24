import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Rogue {
    private final Game game;
    private final Dungeon dungeon;
    private final Scanner scanner;
    private final boolean manualMode;

    // Direction offsets: N, S, W, E, NW, NE, SW, SE, Stay
    // Index:             0  1  2  3   4   5   6   7    8
    private static final int[] DI = {-1, 1, 0, 0, -1, -1, 1, 1, 0};
    private static final int[] DJ = {0, 0, -1, 1, -1, 1, -1, 1, 0};

    // Create a new rogue who is playing a game g (auto mode)
    public Rogue(Game g) {
        this(g, false);
    }

    // Create a new rogue with manual or auto mode
    public Rogue(Game g, boolean manual) {
        this.game = g;
        this.dungeon = g.getDungeon();
        this.manualMode = manual;
        this.scanner = manual ? new Scanner(System.in) : null;
    }

    // Return the adjacent site to which you are moving
    public Site move() {
        if (manualMode) {
            return manualMove();
        } else {
            return autoMove();
        }
    }

    // ============================================================
    // AUTO MODE: Smart Greedy Escape Strategy
    // ============================================================
    
    /**
     * Smart Greedy Algorithm:
     * 
     * 1. Use BFS from MONSTER's position to compute distance to all cells
     *    (This tells us how many moves it takes for monster to reach each cell)
     * 
     * 2. For each legal move the rogue can make, evaluate:
     *    - Primary: Distance from monster (higher = better)
     *    - Secondary: Number of escape routes from that position (more = better)
     *    - Tertiary: Avoid moving toward monster
     * 
     * 3. Pick the best move!
     * 
     * Why this works:
     * - Maximizing distance buys time
     * - Preferring positions with more escape routes avoids getting cornered
     * - Even if caught eventually, rogue survives longer
     */
    private Site autoMove() {
        Site roguePos = game.getRogueSite();
        Site monsterPos = game.getMonsterSite();
        
        // Step 1: Compute BFS distance from monster to all cells
        int[][] distFromMonster = computeBFSDistances(monsterPos);
        
        // Step 2: Get all legal moves for rogue
        ArrayList<Site> legalMoves = dungeon.getLegalMoves(roguePos);
        
        // Also consider staying in place
        legalMoves.add(roguePos);
        
        // Step 3: Evaluate each move and pick the best
        Site bestMove = roguePos;
        int bestDistance = -1;
        int bestEscapeRoutes = -1;
        
        for (Site move : legalMoves) {
            int dist = distFromMonster[move.i()][move.j()];
            int escapeRoutes = dungeon.getLegalMoves(move).size();
            
            // Compare: prefer higher distance, then more escape routes
            if (dist > bestDistance || 
                (dist == bestDistance && escapeRoutes > bestEscapeRoutes)) {
                bestDistance = dist;
                bestEscapeRoutes = escapeRoutes;
                bestMove = move;
            }
        }
        
        return bestMove;
    }
    
    /**
     * BFS to compute shortest distance from a source to all cells.
     * Returns a 2D array where distance[i][j] = shortest path length from source to (i,j)
     * Unreachable cells have distance = -1
     */
    private int[][] computeBFSDistances(Site source) {
        int n = dungeon.size();
        int[][] distance = new int[n][n];
        
        // Initialize all distances to -1 (unreachable)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distance[i][j] = -1;
            }
        }
        
        // BFS from source
        Queue<Site> queue = new LinkedList<>();
        queue.add(source);
        distance[source.i()][source.j()] = 0;
        
        while (!queue.isEmpty()) {
            Site current = queue.remove();
            int currentDist = distance[current.i()][current.j()];
            
            // Explore all neighbors
            for (Site neighbor : dungeon.getLegalMoves(current)) {
                if (distance[neighbor.i()][neighbor.j()] == -1) {
                    distance[neighbor.i()][neighbor.j()] = currentDist + 1;
                    queue.add(neighbor);
                }
            }
        }
        
        return distance;
    }

    // ============================================================
    // MANUAL MODE: Keyboard controls
    // ============================================================
    
    private Site manualMove() {
        Site current = game.getRogueSite();
        
        System.out.println("\n--- ROGUE'S TURN ---");
        System.out.println("Current position: " + current);
        System.out.println("Controls:");
        System.out.println("  W = North    E = East");
        System.out.println("  S = South    Q = West");
        System.out.println("  A = NW       D = NE");
        System.out.println("  Z = SW       C = SE");
        System.out.println("  X = Stay in place");
        System.out.print("Enter move: ");
        
        String input = scanner.nextLine().toUpperCase().trim();
        
        int direction = -1;
        switch (input) {
            case "W": direction = 0; break;  // North
            case "S": direction = 1; break;  // South
            case "Q": direction = 2; break;  // West
            case "E": direction = 3; break;  // East
            case "A": direction = 4; break;  // NW
            case "D": direction = 5; break;  // NE
            case "Z": direction = 6; break;  // SW
            case "C": direction = 7; break;  // SE
            case "X": direction = 8; break;  // Stay
            default:
                System.out.println("Invalid input! Staying in place.");
                return current;
        }
        
        Site newSite = new Site(current.i() + DI[direction], current.j() + DJ[direction]);
        
        if (dungeon.isLegalMove(current, newSite)) {
            return newSite;
        } else {
            System.out.println("Illegal move! Staying in place.");
            return current;
        }
    }

    // ============================================================
    // TESTING
    // ============================================================
    
    public static void main(String[] args) {
        // Check for command line argument for manual mode
        boolean manual = args.length > 0 && args[0].equals("-m");
        
        if (manual) {
            System.out.println("=== MANUAL ROGUE MODE ===");
            System.out.println("Try to escape from the monster!\n");
        } else {
            System.out.println("=== AUTO ROGUE MODE (Smart Greedy) ===");
            System.out.println("Watch the AI rogue try to escape!\n");
            System.out.println("(Run with -m flag for manual mode)\n");
        }
        
        // Test with different dungeons
        String[] dungeonNames = {"SMALL", "CORRIDORS", "TWO_ROOMS", "L_CORRIDOR"};
        String[] dungeons = {
            Game.DUNGEON_SMALL,
            Game.DUNGEON_CORRIDORS, 
            Game.DUNGEON_TWO_ROOMS,
            Game.DUNGEON_L_CORRIDOR
        };
        
        if (manual) {
            // Manual mode - just play one game
            Game game = Game.fromTestDungeon(Game.DUNGEON_TWO_ROOMS);
            Monster monster = new Monster(game);
            Rogue rogue = new Rogue(game, true);
            int turns = game.play(monster, rogue, 50);
            System.out.println("\nYou survived " + turns + " turns!");
        } else {
            // Auto mode - test all dungeons
            for (int d = 0; d < dungeons.length; d++) {
                System.out.println("\n========================================");
                System.out.println("Testing: DUNGEON_" + dungeonNames[d]);
                System.out.println("========================================");
                
                Game game = Game.fromTestDungeon(dungeons[d]);
                Monster monster = new Monster(game);
                Rogue rogue = new Rogue(game, false);
                
                int turns = game.play(monster, rogue, 30);
                System.out.println("Result: Rogue survived " + turns + " turns");
            }
        }
    }
}
