public class Monster {
    public final Game game;
    public final Dungeon dungeon;

    private static int[][] DIRS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};

    // create a new monster who is playing a game g
    public Monster(Game g) {
        this.game = g;
        this.dungeon = g.getDungeon();
    }

    // return the adjacent site to which you are moving (return the site where you are moving)
    public Site move() {
        Site currentSite = game.getMonsterSite();
        Site destinationSite = new Site(0, 0);
        // implement a breath-first search algortithm to find which available direction has the shortest Manhattan distance to the Rogue.
        // conditions: distance must be the closest to the Rogue using manhattan distance

        return destinationSite;
    }
}