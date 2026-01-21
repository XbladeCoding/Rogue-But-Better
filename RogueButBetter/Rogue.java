public class Rogue {
    private final Game game;
    private final Dungeon dungeon;

    // Direction offsets: N, S, W, E, NW, NE, SW, SE
    private static final int[] DI = {-1, 1, 0, 0, -1, -1, 1, 1};
    private static final int[] DJ = {0, 0, -1, 1, -1, 1, -1, 1};

    // Create a new rogue who is playing a game g
    public Rogue(Game g) {
        this.game = g;
        this.dungeon = g.getDungeon();
    }

    // Return the adjacent site to which you are moving
    // PLACEHOLDER: For now, just stay in place
    // TODO: Implement escape strategy (find cycles, maximize distance)
    public Site move() {
        Site currentSite = game.getRogueSite();
        
        // PLACEHOLDER STRATEGY: Stay in place
        // Later we'll implement escape logic
        return currentSite;
    }

    public static void main(String[] args) {
        // Test Rogue with a simple dungeon
        Game game = Game.fromTestDungeon(Game.DUNGEON_SMALL);
        Rogue rogue = new Rogue(game);
        
        System.out.println("Initial game state:");
        game.display();
        
        System.out.println("\nRogue's current position: " + game.getRogueSite());
        Site nextMove = rogue.move();
        System.out.println("Rogue decides to move to: " + nextMove);
        
        // For now, rogue stays in place
        System.out.println("(Placeholder: rogue stays in place)");
    }
}
