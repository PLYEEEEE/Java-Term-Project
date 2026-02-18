

public abstract class Character {

    protected String name;
    protected int maxHearts;
    protected int currentHearts;
    protected float x, y;
    protected float speed;
    protected boolean isAlive = true;

    public Character(String name, int hearts, float x, float y, float speed) {
        this.name = name;
        this.maxHearts = hearts;
        this.currentHearts = hearts;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void move(float dx, float dy) {
        x += dx * speed;
        y += dy * speed;
    }

    public void takeDamage(int amount) {
        currentHearts -= amount;
        if (currentHearts <= 0) {
            isAlive = false;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }
}
