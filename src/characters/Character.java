package characters;



public abstract class Character {

    protected String name;
    protected int maxHearts;
    protected int currentHearts;
    protected float x, y;
    protected float speed;
    protected boolean isAlive = true;
    protected int size = 50;

    public Character(String name, int hearts, float x, float y, float speed) {
        this.name = name;
        this.maxHearts = hearts;
        this.currentHearts = hearts;
        this.x = x;
        this.y = y;
        this.speed = speed;
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

    public int getsize() {
        return size;
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
