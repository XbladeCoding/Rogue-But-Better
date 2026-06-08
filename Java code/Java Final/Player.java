import java.util.ArrayList;

public class Player {
    private int hp;
    private int dmg;
    ArrayList<Item> inventory = new ArrayList<>();

    public void pickUpItem(Item item) {
        inventory.add(item);
    }

    public void takeDmg(int dmgTaken) {
        this.dmg -= dmgTaken;
    }

    public int getDmg() {
        return this.dmg;
    }

    public int getHp() {
        return this.hp;
    }

    public ArrayList<Item> getInventory() {
        return this.inventory;
    }
}