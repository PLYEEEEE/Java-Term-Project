package characters;

public abstract class Character {

    protected String name;
    protected int maxHearts;
    protected int currentHearts;
    protected int worldX, worldY;
    protected float speed;
    protected boolean isAlive = true;
    protected int sizeX;
    protected int sizeY;

    public Character(String name, int hearts, int worldX, int worldY, float speed, int sizeX, int sizeY) {
        this.name = name;
        this.maxHearts = hearts;
        this.currentHearts = hearts;
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public int getWorldX() {
        return worldX;
    }
    public int getWorldY() {
        return worldY;
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }
    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }

    public float getSpeed() {
        return speed;
    }   

    public float getSizeX() {
        return sizeX;
    }
    public float getSizeY() {
        return sizeY;
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
