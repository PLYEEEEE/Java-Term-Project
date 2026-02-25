package characters;
/**
 * Concrete class for slime monsters.
 * Provides core functionality for HP, damage, movement.
 * 
 * Stats scale infinitely with wave number:
 * - HP increases by 10% per wave
 * - Damage increases by 8% per wave
 * 
 * Animations:
 * - MovingAnim.gif  : เมื่อ slime กำลังเคลื่อนที่
 * - AttackAnim.gif  : เมื่อ slime โจมตี
 * - AttackedAnim.gif: เมื่อ slime ถูกโจมตี
 * - DeadAnim.gif    : เมื่อ slime ตาย
 * - IdleAnim.gif    : สถานะปกติ (ไม่ทำอะไร)
 */
import java.awt.*;
import javax.swing.ImageIcon;
import java.net.URL;

public class Slime {
    private int sizeX;
    private int sizeY;

    // ===== Animation State =====
    public enum State {
        IDLE, MOVING, ATTACKING, ATTACKED, DEAD
    }

    private State currentState = State.IDLE;

    // Animated GIF images (ImageIcon handles frame cycling automatically)
    private ImageIcon idleAnim;
    private ImageIcon movingAnim;
    private ImageIcon attackAnim;
    private ImageIcon attackedAnim;
    private ImageIcon deadAnim;

    // Tracks how long the current one-shot state has been active (ms)
    private long stateStartTime = 0;
    private static final int ATTACK_ANIM_DURATION   = 600; // ms
    private static final int ATTACKED_ANIM_DURATION = 500; // ms
    private static final int DEAD_ANIM_DURATION     = 800; // ms before hiding

    // ===== Facing direction =====
    private int facing = 1; // 1 = right, -1 = left

    // ImageObserver สำหรับให้ GIF animate (ส่ง GamePanel มาจาก GamePanel)
    private java.awt.image.ImageObserver observer;
    public void setObserver(java.awt.image.ImageObserver obs) { this.observer = obs; }

    // ===== Load helpers =====
    private ImageIcon loadGif(String path) {
        URL url = getClass().getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        }
        // Fallback: try class-loader
        url = getClass().getClassLoader().getResource(path.startsWith("/") ? path.substring(1) : path);
        if (url != null) {
            return new ImageIcon(url);
        }
        return null;
    }

    private void loadAnimations() {
        // Path ตรงกับ folder จริงใน project: src/Image/Monsters/BlueSlime/Slime/
        idleAnim     = loadGif("/Image/Monsters/BlueSlime/IdleAnim.gif");
        movingAnim   = loadGif("/Image/Monsters/BlueSlime/MovingAnim.gif");
        attackAnim   = loadGif("/Image/Monsters/BlueSlime/AttackAnim.gif");
        attackedAnim = loadGif("/Image/Monsters/BlueSlime/AttackedAnim.gif");
        deadAnim     = loadGif("/Image/Monsters/BlueSlime/DeadAnim.gif");

        // Debug — จะพิมพ์ null ถ้าโหลดไม่ได้
        System.out.println("[Slime] idleAnim     = " + idleAnim);
        System.out.println("[Slime] movingAnim   = " + movingAnim);
        System.out.println("[Slime] attackAnim   = " + attackAnim);
        System.out.println("[Slime] attackedAnim = " + attackedAnim);
        System.out.println("[Slime] deadAnim     = " + deadAnim);
    }

    // ===== State machine =====
    public void setState(State newState) {
        if (currentState == State.DEAD && newState != State.DEAD) return; // dead stays dead
        if (currentState != newState) {
            currentState = newState;
            stateStartTime = System.currentTimeMillis();
        }
    }

    /** Call once per frame to advance the state machine */
    public void updateState() {
        long elapsed = System.currentTimeMillis() - stateStartTime;
        switch (currentState) {
            case ATTACKING:
                if (elapsed >= ATTACK_ANIM_DURATION) {
                    setState(State.IDLE);
                }
                break;
            case ATTACKED:
                if (elapsed >= ATTACKED_ANIM_DURATION) {
                    setState(isDead ? State.DEAD : State.IDLE);
                }
                break;
            case DEAD:
                // stay in dead — draw() will stop rendering after DEAD_ANIM_DURATION
                break;
            default:
                break;
        }
    }

    /** Returns the ImageIcon that matches the current animation state */
    private ImageIcon getCurrentAnim() {
        switch (currentState) {
            case MOVING:    return movingAnim   != null ? movingAnim   : idleAnim;
            case ATTACKING: return attackAnim   != null ? attackAnim   : idleAnim;
            case ATTACKED:  return attackedAnim != null ? attackedAnim : idleAnim;
            case DEAD:      return deadAnim     != null ? deadAnim     : idleAnim;
            default:        return idleAnim;
        }
    }

    // ===== Draw =====
    public void draw(Graphics2D g2, Knight knight) {
        // After the dead animation finishes, stop drawing entirely
        if (isDead) {
            long elapsed = System.currentTimeMillis() - stateStartTime;
            if (currentState != State.DEAD) {
                // make sure state is DEAD
                setState(State.DEAD);
            }
            if (elapsed > DEAD_ANIM_DURATION) return; // fully gone
        }

        int screenX = (int)(pointsWorldX - knight.getWorldX() + knight.screenX);
        int screenY = (int)(pointsWorldY - knight.getWorldY() + knight.screenY);

        ImageIcon anim = getCurrentAnim();

        if (anim != null) {
            Image img = anim.getImage();
            Graphics2D g2d = (Graphics2D) g2.create();

            if (facing > 0) {
                // Flip horizontally
                g2d.translate(screenX + sizeX, screenY);
                g2d.scale(-1, 1);
                g2d.drawImage(img, 0, 0, sizeX, sizeY, observer);
            } else {
                g2d.drawImage(img, screenX, screenY, sizeX, sizeY, observer);
            }
            g2d.dispose();
        } else {
            // Fallback: draw plain oval (original behaviour)
            g2.setColor(new Color(0, 200, 0));
            if (iFrame) g2.setColor(Color.RED);
            g2.fillOval(screenX, screenY, sizeX, sizeY);
            g2.setColor(Color.BLACK);
            g2.drawOval(screenX, screenY, sizeX, sizeY);
        }

        // HP bar (always drawn while alive)
        if (!isDead) {
            int barWidth = 40, barHeight = 6;
            int hpBarX = screenX + sizeX / 2 - barWidth / 2;
            int hpBarY = screenY - 10;
            float hpPercent = Math.max(0, Math.min(1, currentHP / maxHP));
            g2.setColor(Color.RED);
            g2.fillRect(hpBarX, hpBarY, barWidth, barHeight);
            g2.setColor(Color.GREEN);
            g2.fillRect(hpBarX, hpBarY, (int)(barWidth * hpPercent), barHeight);
            g2.setColor(Color.BLACK);
            g2.drawRect(hpBarX, hpBarY, barWidth, barHeight);
        }
    }

    // ===== Base Stats (before multipliers) =====
    private static final float BASE_HP         = 3f;
    private static final float BASE_ATTACK     = 15f;
    private static final float BASE_MOVE_SPEED = 2f;

    // ===== Scaling Constants =====
    private static final float WAVE_HP_SCALE  = 0.10f;
    private static final float WAVE_DMG_SCALE = 0.08f;

    // ===== Protected Fields =====
    protected float maxHP;
    protected float currentHP;
    protected float attackDamage;
    protected float moveSpeed;
    protected float attackRange;
    protected float positionX;
    protected float positionY;
    protected int   pointsWorldX;
    protected int   pointsWorldY;
    protected boolean isDead;
    protected int attackCooldown = 3000;
    private   int iFrameDuration = 500;
    public  boolean iFrame = false;
    public  long    iFrameStart;

    public void upDateIFrame() {
        long now = System.currentTimeMillis();
        if (iFrame && now - iFrameStart >= iFrameDuration) {
            iFrame = false;
        }
    }

    protected int currentWave;

    // ===== Constructor =====
    public Slime(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.currentWave  = 1;
        this.pointsWorldX = 0;
        this.pointsWorldY = 0;
        this.isDead       = false;
        loadAnimations();
        calculateBaseStats();
        applyScaling();
        this.currentHP = this.maxHP;
    }

    // ===== Base Stats Calculation =====
    protected void calculateBaseStats() { /* constants already defined */ }

    // ===== Stat Scaling =====
    private void applyScaling() {
        float waveHPFactor  = 1f + (currentWave - 1) * WAVE_HP_SCALE;
        float waveDmgFactor = 1f + (currentWave - 1) * WAVE_DMG_SCALE;
        maxHP        = BASE_HP    * waveHPFactor;
        attackDamage = BASE_ATTACK * waveDmgFactor;
        moveSpeed    = BASE_MOVE_SPEED;
        attackRange  = 50f * 0.5f;
    }

    public void updateStatsForWave(int waveNumber) {
        this.currentWave = Math.max(1, waveNumber);
        float oldMaxHP   = maxHP;
        applyScaling();
        if (oldMaxHP > 0) {
            currentHP = maxHP * (currentHP / oldMaxHP);
        } else {
            currentHP = maxHP;
        }
    }

    // ===== Combat Methods =====
    public void takeDamage(float damage) {
        if (isDead) return;
        currentHP -= damage;
        setState(State.ATTACKED); // show attacked animation
        if (currentHP <= 0) {
            currentHP = 0;
            die();
        }
    }

    public float attack() {
        if (isDead) return 0f;
        setState(State.ATTACKING); // show attack animation
        float variance = 0.9f + (float)(Math.random() * 0.2f);
        return attackDamage * variance;
    }

    public void die() {
        if (isDead) return;
        isDead = true;
        setState(State.DEAD); // show dead animation
        currentHP = 0;
    }

    // ===== Movement =====
    /**
     * Call this every frame from GamePanel to set MOVING or IDLE state automatically.
     * @param isMoving true when the slime moved this frame
     */
    public void notifyMoving(boolean isMoving) {
        if (isDead) return;
        if (isMoving && currentState == State.IDLE) {
            setState(State.MOVING);
        } else if (!isMoving && currentState == State.MOVING) {
            setState(State.IDLE);
        }
    }

    /** Set which direction the slime is facing (1=right, -1=left) so sprite can be flipped */
    public void setFacing(int dir) {
        this.facing = dir;
    }

    public void setPositionWorld(int x, int y) {
        this.pointsWorldX = x;
        this.pointsWorldY = y;
    }

    // ===== Getters =====
    public int  getAttackCooldown() { return attackCooldown; }
    public boolean isDead()         { return isDead; }
    public float getCurrentHP()     { return currentHP; }
    public float getMaxHP()         { return maxHP; }
    public float getAttackDamage()  { return attackDamage; }
    public float getMoveSpeed()     { return moveSpeed; }
    public float getAttackRange()   { return attackRange; }
    public float getPositionX()     { return positionX; }
    public float getPositionY()     { return positionY; }
    public int   getPointsWorldX()  { return pointsWorldX; }
    public int   getPointsWorldY()  { return pointsWorldY; }
    public int   getCurrentWave()   { return currentWave; }
    public float getHPPercentage()  { return maxHP > 0 ? (currentHP / maxHP) * 100f : 0f; }
    public float getSizeX()         { return sizeX; }
    public float getSizeY()         { return sizeY; }

    // ===== Setters =====
    public void setPointsWorldPosition(int x, int y) {
        this.pointsWorldX = x;
        this.pointsWorldY = y;
    }

    @Override
    public String toString() {
        return String.format("%s[Wave%d | HP:%.0f/%.0f ATK:%.1f SPD:%.1f]",
            getClass().getSimpleName(), currentWave,
            currentHP, maxHP, attackDamage, moveSpeed);
    }

    private long lastDamageTime = 0;
    public long getLastDamageTime()   { return lastDamageTime; }
    public void setLastDamageTime(long t) { lastDamageTime = t; }
}