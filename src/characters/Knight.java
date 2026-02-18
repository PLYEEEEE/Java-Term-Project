package characters;

import java.awt.*;

public class Knight extends Character {

    private float attackRange = 100;
    private float skillRange = 180;

    private boolean attacking = false;
    private boolean usingSkill = false;
    private boolean hasDealtDamage = false;

    private long attackStart;
    private long skillStart;
    private long skillCooldown = 10000;
    private long lastSkillTime = 0;

    private int facing = 1; // 1 = right, -1 = left

    public Knight(String name, float x, float y) {
        super(name, 5, x, y, 3.5f);
    }

    public void setFacing(int dir) {
        facing = dir;
    }

    public void attack() {
        if (!usingSkill) {
            attacking = true;
            hasDealtDamage = false;  // รีเซ็ตตอนเริ่มโจมตี
            attackStart = System.currentTimeMillis();
        }
    }

    public void useSkill() {
        long now = System.currentTimeMillis();
        if (!attacking && !usingSkill &&
                now - lastSkillTime >= skillCooldown) {

            usingSkill = true;
            hasDealtDamage = false;  // รีเซ็ตตอนเริ่มสกิล
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
        g2.fillOval((int)x, (int)y, 40, 40);

        // โจมตีครึ่งวงกลม
        if (attacking) {
            g2.setColor(new Color(255, 0, 0, 120));

            int startAngle = (facing == 1) ? -90 : 90;

            g2.fillArc(
                    (int)(x - attackRange/2 + 20),
                    (int)(y - attackRange/2 + 20),
                    (int)attackRange,
                    (int)attackRange,
                    startAngle,
                    180
            );
        }

        // สกิล 360 องศา
        if (usingSkill) {
            g2.setColor(new Color(0, 255, 255, 120));

            g2.fillOval(
                    (int)(x - skillRange/2 + 20),
                    (int)(y - skillRange/2 + 20),
                    (int)skillRange,
                    (int)skillRange
            );
        }
    }

    // ======================
    // Getter ที่ GamePanel ใช้
    // ======================

    public boolean isAttacking() {
        return attacking;
    }

    public boolean isUsingSkill() {
        return usingSkill;
    }

    public boolean hasDealtDamage() {
        return hasDealtDamage;
    }

    public void setHasDealtDamage(boolean value) {
        hasDealtDamage = value;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public float getSkillRange() {
        return skillRange;
    }
}
