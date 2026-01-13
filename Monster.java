public class Monster {
    public final Game game;
    public final Dungeon dungeon;

    public Monster(Game g) {
        this.game = g;
        this.dungeon = g.getDungeon();
    }
}