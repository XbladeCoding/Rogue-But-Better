public class Monster {
    public int points;
    public int hp;
    public int dmg;
    public String type;

    public Monster(int hp, int dmg, String type, int points) {
        this.hp = hp;
        this.dmg = dmg;
        this.type = type;
        this.points = points;
    }

    public void takeDamage(int dmgTaken) {
        this.hp -= dmgTaken;
    }

    public int getDmg() {
        return dmg;
    }

    public int getHp() {
        return hp;
    }

    public int getPoints() {
        return points;
    }

    public String getType() {
        return type;
    }
}
