import java.util.ArrayList;

public class Dungeon {
    private static final char ROOM_SYMBOL = '.';
    private static final char CORRIDOR_SYMBOL = '+';
    private static final char WALL_SYMBOL = ' ';

    private final char[][] grid;
    private final int size;

    public Dungeon(char[][] userMatrix) {
        this.size = userMatrix.length;
        this.grid = new char[size][size];
        for (int row = 0; row < size; row++) {
            System.arraycopy(userMatrix[row], 0, this.grid[row], 0, size);
        }
    }

    // Check if a site is within the dungeon bounds
    public boolean inBounds(Site v) {
        return v.i() >= 0 && v.i() < size && v.j() >= 0 && v.j() < size;
    }

    // Check if move is to an adjacent site (including diagonal)
    private boolean isAdjacent(Site v, Site w) {
        int di = Math.abs(v.i() - w.i());
        int dj = Math.abs(v.j() - w.j());
        // Adjacent means at most 1 step in each direction, but not staying in place
        return di <= 1 && dj <= 1 && (di + dj > 0);
    }

    // Check if move is diagonal
    private boolean isDiagonal(Site v, Site w) {
        int di = Math.abs(v.i() - w.i());
        int dj = Math.abs(v.j() - w.j());
        return di == 1 && dj == 1;
    }

    // is moving from site v to w legal?
    public boolean isLegalMove(Site v, Site w) {
        // Check bounds
        if (!inBounds(v) || !inBounds(w)) {
            return false;
        }

        // Staying in place is always legal
        if (v.equals(w)) {
            return true;
        }

        // Must be adjacent
        if (!isAdjacent(v, w)) {
            return false;
        }

        // Cannot move to a wall
        if (isWall(w)) {
            return false;
        }

        // If starting from corridor, cannot move diagonally
        if (isCorridor(v) && isDiagonal(v, w)) {
            return false;
        }

        // If moving to corridor, cannot move diagonally
        if (isCorridor(w) && isDiagonal(v, w)) {
            return false;
        }

        return true;
    }

    // is site v a corridor site? (cannot move diagonally from/to corridor)
    public boolean isCorridor(Site v) {
        if (!inBounds(v)) return false;
        return grid[v.i()][v.j()] == CORRIDOR_SYMBOL;
    }

    // is site v a room site? (can move diagonally within rooms)
    public boolean isRoom(Site v) {
        if (!inBounds(v)) return false;
        return grid[v.i()][v.j()] == ROOM_SYMBOL;
    }

    // is site v a wall? (cannot move to walls)
    public boolean isWall(Site v) {
        if (!inBounds(v)) return true; // Out of bounds is treated as wall
        return grid[v.i()][v.j()] == WALL_SYMBOL;
    }

    // return N = dimension of dungeon (n x n)
    public int size() {
        return size;
    }

    // Get character at a site (useful for finding rogue/monster)
    public char getChar(Site v) {
        if (!inBounds(v)) return WALL_SYMBOL;
        return grid[v.i()][v.j()];
    }

    // Get all legal moves from a given site
    // Returns an ArrayList of all sites the player can move to
    public ArrayList<Site> getLegalMoves(Site v) {
        ArrayList<Site> moves = new ArrayList<>();
        
        // 8 possible directions: N, S, E, W, NE, NW, SE, SW
        // Each direction is represented as (di, dj) offset
        int[] di = {-1, 1, 0, 0, -1, -1, 1, 1};  // row offsets
        int[] dj = {0, 0, -1, 1, -1, 1, -1, 1};  // col offsets
        
        for (int d = 0; d < 8; d++) {
            Site neighbor = new Site(v.i() + di[d], v.j() + dj[d]);
            if (isLegalMove(v, neighbor)) {
                moves.add(neighbor);
            }
        }
        
        return moves;
    }

    // Display the dungeon
    public void display() {
        System.out.println("Dungeon (" + size + "x" + size + "):");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Create a simple 5x5 test dungeon
        // Layout:
        //   +++++
        //   +...+
        //   +...+
        //   +...+
        //   +++++
        char[][] testGrid = {
            {' ', '+', '+', '+', ' '},
            {'+', '.', '.', '.', '+'},
            {'+', '.', '.', '.', '+'},
            {'+', '.', '.', '.', '+'},
            {' ', '+', '+', '+', ' '}
        };

        Dungeon dungeon = new Dungeon(testGrid);
        dungeon.display();

        System.out.println("\n--- Testing Site Types ---");
        Site room = new Site(2, 2);      // Center room
        Site corridor = new Site(0, 2);  // Top corridor
        Site wall = new Site(0, 0);      // Corner wall

        System.out.println("Site " + room + " isRoom: " + dungeon.isRoom(room));           // true
        System.out.println("Site " + corridor + " isCorridor: " + dungeon.isCorridor(corridor)); // true
        System.out.println("Site " + wall + " isWall: " + dungeon.isWall(wall));           // true

        System.out.println("\n--- Testing Legal Moves ---");
        Site roomA = new Site(2, 2);
        Site roomB = new Site(1, 1);  // Diagonal from roomA
        Site roomC = new Site(2, 3);  // Adjacent to roomA (east)
        Site corr = new Site(1, 0);   // Corridor

        // Room to room diagonal - LEGAL
        System.out.println(roomA + " -> " + roomB + " (room diagonal): " + dungeon.isLegalMove(roomA, roomB)); // true

        // Room to room adjacent - LEGAL  
        System.out.println(roomA + " -> " + roomC + " (room adjacent): " + dungeon.isLegalMove(roomA, roomC)); // true

        // Room to corridor (not diagonal) - LEGAL
        Site roomNearCorr = new Site(1, 1);
        System.out.println(roomNearCorr + " -> " + corr + " (room to corridor): " + dungeon.isLegalMove(roomNearCorr, corr)); // true

        // Corridor to room diagonal - ILLEGAL
        Site corrTop = new Site(0, 1);
        Site roomDiag = new Site(1, 2);
        System.out.println(corrTop + " -> " + roomDiag + " (corridor diagonal): " + dungeon.isLegalMove(corrTop, roomDiag)); // false

        // Move to wall - ILLEGAL
        System.out.println(roomA + " -> " + wall + " (to wall): " + dungeon.isLegalMove(roomA, wall)); // false

        // Stay in place - LEGAL
        System.out.println(roomA + " -> " + roomA + " (stay): " + dungeon.isLegalMove(roomA, roomA)); // true

        // ========== Phase 4: Test getLegalMoves ==========
        System.out.println("\n--- Testing getLegalMoves ---");
        
        // From center room (2,2) - should have 8 moves (all room neighbors)
        Site center = new Site(2, 2);
        ArrayList<Site> centerMoves = dungeon.getLegalMoves(center);
        System.out.println("Legal moves from " + center + " (center room): " + centerMoves);
        System.out.println("Number of moves: " + centerMoves.size()); // Should be 8
        
        // From corridor (0,2) - should have only 2 moves (left and right corridors, no diagonal)
        Site topCorr = new Site(0, 2);
        ArrayList<Site> corrMoves = dungeon.getLegalMoves(topCorr);
        System.out.println("\nLegal moves from " + topCorr + " (top corridor): " + corrMoves);
        System.out.println("Number of moves: " + corrMoves.size()); // Should be 2 (left/right)
        
        // From corner corridor (0,1) - limited moves
        Site cornerCorr = new Site(0, 1);
        ArrayList<Site> cornerMoves = dungeon.getLegalMoves(cornerCorr);
        System.out.println("\nLegal moves from " + cornerCorr + " (corner corridor): " + cornerMoves);
        System.out.println("Number of moves: " + cornerMoves.size());
    }
}
