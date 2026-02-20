package characters;



public abstract class Character {

    protected String name;
    protected int maxHearts;
    protected int currentHearts;
    protected float x, y;
    protected float speed;
    protected boolean isAlive = true;
    protected int sizeX;
    protected int sizeY;

    public Character(String name, int hearts, float x, float y, float speed, int sizeX, int sizeY) {
        this.name = name;
        this.maxHearts = hearts;
        this.currentHearts = hearts;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
       
    }
    public void setY(float y) {
        this.y = y;
        
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
