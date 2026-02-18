import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Monster {
    private final Game game;
    private final Dungeon dungeon;

    // Create a new monster who is playing a game g
    public Monster(Game g) {
        this.game = g;
        this.dungeon = g.getDungeon();
    }

    /**
     * BFS (Breadth-First Search) to find shortest path to the rogue.
     * 
     * How BFS works (like ripples in water):
     * 1. Start at monster's position
     * 2. Look at ALL neighbors 1 step away
     * 3. Look at ALL neighbors 2 steps away
     * 4. Keep going until we find the rogue
     * 5. The first path we find is guaranteed to be the shortest!
     * 
     * We use a QUEUE (first-in, first-out) to process sites in order.
     * Think of it like a line at a store - first person in line gets served first.
     */
    public Site move() {
        Site monsterPos = game.getMonsterSite();
        Site roguePos = game.getRogueSite();
        int n = dungeon.size();

        // ============================================================
        // STEP 1: Set up our data structures
        // ============================================================
        
        // visited[i][j] = true if we've already looked at site (i,j)
        // This prevents us from going in circles!
        boolean[][] visited = new boolean[n][n];
        
        // parent[i][j] = the site we came FROM to reach site (i,j)
        // This lets us trace back the path once we find the rogue
        Site[][] parent = new Site[n][n];
        
        // The queue holds sites we need to explore
        // LinkedList works as a queue: add to back, remove from front
        Queue<Site> queue = new LinkedList<>();

        // ============================================================
        // STEP 2: Start BFS from the monster's position
        // ============================================================
        
        // Mark monster's starting position as visited
        visited[monsterPos.i()][monsterPos.j()] = true;
        
        // Add monster's position to the queue (this is where we start)
        queue.add(monsterPos);
        
        // The monster's position has no parent (it's the starting point)
        parent[monsterPos.i()][monsterPos.j()] = null;

        // ============================================================
        // STEP 3: Explore the dungeon level by level
        // ============================================================
        
        // Keep going while there are sites left to explore
        while (!queue.isEmpty()) {
            
            // Take the next site from the front of the queue
            Site current = queue.remove();
            
            // Did we find the rogue?
            if (current.equals(roguePos)) {
                // Yes! Now trace back to find the first step
                // (See STEP 4 below)
                break;
            }
            
            // Get all legal moves from the current site
            ArrayList<Site> neighbors = dungeon.getLegalMoves(current);
            
            // Look at each neighbor
            for (Site neighbor : neighbors) {
                
                // Have we visited this neighbor before?
                if (!visited[neighbor.i()][neighbor.j()]) {
                    
                    // No! Mark it as visited
                    visited[neighbor.i()][neighbor.j()] = true;
                    
                    // Remember how we got here (from 'current')
                    parent[neighbor.i()][neighbor.j()] = current;
                    
                    // Add this neighbor to the queue to explore later
                    queue.add(neighbor);
                }
            }
        }

        // ============================================================
        // STEP 4: Trace back to find the FIRST step toward the rogue
        // ============================================================
        
        // Did we find a path to the rogue?
        if (!visited[roguePos.i()][roguePos.j()]) {
            // No path found! Stay in place.
            // (This shouldn't happen in a valid dungeon)
            return monsterPos;
        }
        
        // Start from the rogue and walk backwards to the monster
        // We want to find the site that is ONE step away from the monster
        Site step = roguePos;
        
        // Keep going back until we're one step away from the monster
        while (parent[step.i()][step.j()] != null && 
               !parent[step.i()][step.j()].equals(monsterPos)) {
            // Move one step back toward the monster
            step = parent[step.i()][step.j()];
        }
        
        // 'step' is now the first move the monster should make!
        return step;
    }

    // ============================================================
    // TEST: Let's see the monster chase the rogue!
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Testing Monster BFS Strategy ===\n");
        
        // Create a game
        Game game = Game.fromTestDungeon(Game.DUNGEON_SMALL);
        Monster monster = new Monster(game);
        
        System.out.println("Initial state:");
        game.display();
        
        // See what move the monster chooses
        System.out.println("\nMonster is at: " + game.getMonsterSite());
        System.out.println("Rogue is at: " + game.getRogueSite());
        
        Site nextMove = monster.move();
        System.out.println("Monster's BFS says move to: " + nextMove);
        
        // Let's simulate a few moves manually
        System.out.println("\n--- Simulating monster chasing rogue (rogue stays still) ---");
        for (int turn = 1; turn <= 5; turn++) {
            Site move = monster.move();
            System.out.println("Turn " + turn + ": Monster moves from " + 
                             game.getMonsterSite() + " to " + move);
            game.setMonsterSite(move);
            game.display();
            
            if (game.isGameOver()) {
                System.out.println("*** Monster caught the rogue! ***");
                break;
            }
        }
    }
}