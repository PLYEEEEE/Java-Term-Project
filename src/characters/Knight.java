package characters;

import java.awt.*;

public class Knight extends Character {

    private int health = 10;
    private final int maxHealth = 10;

    public int screenX;
    public int screenY;

    private float attackRange = 150;
    private float skillRange = 300;

    private boolean attacking = false;
    private boolean usingSkill = false;

    private long attackStart;
    private long skillStart;
    private long skillCooldown = 10000;
    private long lastSkillTime = 0;

    private int facing = 1; // 1 = right, -1 = left

    public Knight(String name, int worldX, int worldY, int sizex, int sizey, int screenWidth, int screenHeight) {
        super(name, 5, worldX, worldY, 3.0f, sizex, sizey);
        this.screenX = screenWidth / 2 - sizex / 2;
        this.screenY = screenHeight / 2 - sizey / 2;
    }

    public void setFacing(int dir) {
        facing = dir;
    }

    public void attack() {
        if (!usingSkill) {
            attacking = true;
            attackStart = System.currentTimeMillis();
        }
    }

    public void useSkill() {
        long now = System.currentTimeMillis();
        if (!attacking && !usingSkill &&
                now - lastSkillTime >= skillCooldown) {

            usingSkill = true;
            skillStart = now;
            lastSkillTime = now;
        }
    }

    
    public void update() {
        long now = System.currentTimeMillis();

        if (attacking && now - attackStart > 250) {
            attacking = false;
        }

        if (usingSkill && now - skillStart > 400) {
            usingSkill = false;
        }
    }

    public void draw(Graphics2D g2) {

        // ตัวละคร
        g2.setColor(Color.BLUE);
        g2.fillOval(screenX, screenY,(int)getSizeX(), (int)getSizeY());

        // โจมตีครึ่งวงกลม
        if (attacking) {
            g2.setColor(new Color(255, 0, 0, 120));

            int startAngle = (facing == 1) ? -90 : 90;
            g2.fillArc(
                    (int)(screenX - attackRange/2 + getSizeX()/2),
                    (int)(screenY - attackRange/2 + getSizeY()/2),
                    (int)attackRange,
                    (int)attackRange,
                    startAngle,
                    180
            );
        }

        // สกิล 360
        if (usingSkill) {
            g2.setColor(new Color(0, 255, 255, 120));
            g2.fillOval(
                    (int)(screenX - skillRange/2 + getSizeX()/2),
                    (int)(screenY - skillRange/2 + getSizeY()/2),
                    (int)skillRange,
                    (int)skillRange
            );
        }

        // บาร์สุขภาพ
        g2.setColor(Color.RED);
        int barWidth = (int)getSizeX();
        int barHeight = 8;
        int barX = (int)screenX;
        int barY = (int)screenY - barHeight - 5;
        float healthPercent = (float)health / maxHealth;
        g2.drawRect(barX, barY, barWidth, barHeight);
        g2.setColor(Color.GREEN);
        g2.fillRect(barX + 1, barY + 1, (int)((barWidth - 1) * healthPercent), barHeight - 1);
    }

    public void reset() {
        this.health = maxHealth;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }
    
    public float getSizeX() {
        return sizeX;
    }
    public float getSizeY() {
        return sizeY;
    }

    public int getHealth() {
        return health;
    }

    public int getScreenX() {
        return screenX;
    }
    public int getScreenY() {
        return screenY;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

}
