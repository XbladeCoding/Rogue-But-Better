public class Rogue {
    public final Game game;
    public final Dungeon dungeon;
    // create a new rogue who is playing a game g
    public Rogue(Game g) {
        this.game = g;
        this.dungeon = g.getDungeon();
    }
    // return the adjacent site to which you are moving
    public Site move() {
        Site currentSite = game.getMonsterSite();
        Site destinationSite;

        // implement a breath-first search algortithm to find which available direction has the best evasive maneuver in the current situation
        // the manhattan distance to the monster must be the longest and the rogue 5x5 square area must be free of walls (unless there is a corridor).

        return destinationSite;
    }
}
