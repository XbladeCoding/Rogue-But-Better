public class Dungeon {
    private static final char ROOM_SYMBOL = '.';
    private static final char CORRIDOR_SYMBOL = '+';
    private static final char MONSTER_SYMBOL = 'M';

    private final char[][] grid;
    private final int size;

    public Dungeon(char[][] userMatrix) {
        this.size = userMatrix.length;
        this.grid = new char[size][size];
        for (int row = 0; row < size; row++) {
            System.arraycopy(userMatrix[row], 0, this.grid[row], 0, size);
        }
    }

    public boolean isLegalMove(Site v, Site w) {
        // Cannot move diagonally if one of sites is a corridor
        // Three cases to check: one is a corridor (legal if manhattan distance is 1), both are rooms (legal if 
        // manhattan distance is 1 or 2), one is a wall (illegal)
        if (isCorridor(v) || isCorridor(w)) {
            return (v.manhattan(w) == 1);
        } else if (isRoom(v) && isRoom(w)) {
            int dist = v.manhattan(w);
            return (dist == 1 || dist == 2);
        } else {
            return false;
        }
    }
    // is moving from site v to w legal? (use isCorridor and isRoom to check)
    public boolean isCorridor(Site v) {
        return (grid[v.i()][v.j()] == CORRIDOR_SYMBOL);
    }
    // is site v a corridor site? (cannot move to diagonally)
    public boolean isRoom(Site v) {
        return (grid[v.i()][v.j()] == ROOM_SYMBOL);
    }
    public boolean isMonsterSite(Site v) {
        return (grid[v.i()][v.j()] == MONSTER_SYMBOL);
    }
    public boolean isRogueSite(Site v) {
        return (grid[v.i()][v.j()] == '@');
    }
    // is site v a room site? (can move to diagonally)
    public int size() {
        return grid.length;
    }
    // return N = dimension of dungeon (n x n)
}
