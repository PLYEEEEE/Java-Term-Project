package characters;
/**
 * Concrete class for slime monsters.
 * Provides core functionality for HP, damage, movement.
 * 
 * Stats scale infinitely with wave number:
 * - HP increases by 10% per wave
 * - Damage increases by 8% per wave
 */
import java.awt.*;

public class Slime {
        private int sizeX;
        private int sizeY;
        
        public void draw(Graphics2D g2,Knight knight) {
            if (isDead) return;
            g2.setColor(new Color(0, 200, 0));
            if (iFrame) {
                g2.setColor(Color.RED);
            }
            int screenX = (int)(pointsWorldX - knight.getWorldX() + knight.screenX);
            int screenY = (int)(pointsWorldY - knight.getWorldY() + knight.screenY);
            g2.fillOval(screenX, screenY, sizeX, sizeY);

            // วาดขอบ
            g2.setColor(Color.BLACK);
            g2.drawOval(screenX, screenY, sizeX, sizeY);

            // วาด HP bar
            int barWidth = 40, barHeight = 6;
            int hpBarX = screenX + sizeX/2 - barWidth/2;
            int hpBarY = screenY - 10;
            float hpPercent = Math.max(0, Math.min(1, currentHP / maxHP));
            g2.setColor(Color.RED);
            g2.fillRect(hpBarX, hpBarY, barWidth, barHeight);
            g2.setColor(Color.GREEN);
            g2.fillRect(hpBarX, hpBarY, (int)(barWidth * hpPercent), barHeight);
            g2.setColor(Color.BLACK);
            g2.drawRect(hpBarX, hpBarY, barWidth, barHeight);
        }
    
    // ===== Base Stats (before multipliers) =====
    private static final float BASE_HP = 3f;
    private static final float BASE_ATTACK = 15f;
    private static final float BASE_MOVE_SPEED = 2f;
    // EXP removed
    
    // ===== Scaling Constants =====
    private static final float WAVE_HP_SCALE = 0.10f;    // 10% per wave
    private static final float WAVE_DMG_SCALE = 0.08f;   // 8% per wave
    // LEVEL_SCALE removed
    
    // ===== Protected Fields =====
    protected float maxHP;
    protected float currentHP;
    protected float attackDamage;
    protected float moveSpeed;
    protected float attackRange;
    protected float positionX;
    protected float positionY;
    protected int pointsWorldX;
    protected int pointsWorldY;
    protected boolean isDead;
    protected int attackCooldown = 3000;
    private int iFrameDuration = 500;
    public boolean iFrame = false;
    public long iFrameStart;

    public void upDateIFrame(){
        long now = System.currentTimeMillis();
        if (iFrame && now - iFrameStart >= iFrameDuration) {
            iFrame = false;
        }
    }
    
    // Current wave number (for stat scaling)
    protected int currentWave;
    
    // ===== Constructor =====
    
    /**
     * Creates a new slime. Uses default medium size and melee type.
     */
    public Slime(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.currentWave = 1;
        this.pointsWorldX = 0;
        this.pointsWorldY = 0;
        this.isDead = false;
        calculateBaseStats();
        applyScaling();
        this.currentHP = this.maxHP;
    }
    
    // ===== Base Stats Calculation =====
    
    /**
     * Calculates base stats before size multipliers and scaling.
     * For standard slime, no special calculations needed.
     */
    protected void calculateBaseStats() {
        // Base stats are already defined as constants
        // No special calculations needed for standard slime
    }
    
    // ===== Stat Scaling =====
    
    /**
     * Applies wave multipliers to stats.
     * Called automatically on construction and when wave changes.
     */
    private void applyScaling() {
        float waveHPFactor = 1f + (currentWave - 1) * WAVE_HP_SCALE;
        float waveDmgFactor = 1f + (currentWave - 1) * WAVE_DMG_SCALE;
        maxHP = BASE_HP * waveHPFactor;
        attackDamage = BASE_ATTACK * waveDmgFactor;
        moveSpeed = BASE_MOVE_SPEED;
        attackRange = 50f * 0.5f; // Default attack range for standard slime
    }
    
    /**
     * Updates stats for a new wave number.
     * This allows infinite scaling as waves progress.
     * 
     * @param waveNumber The new wave number
     */
    public void updateStatsForWave(int waveNumber) {
        this.currentWave = Math.max(1, waveNumber);
        float oldMaxHP = maxHP;
        applyScaling();
        
        // Scale current HP proportionally
        if (oldMaxHP > 0) {
            float hpRatio = currentHP / oldMaxHP;
            currentHP = maxHP * hpRatio;
        } else {
            currentHP = maxHP;
        }
    }
    
    // ===== Combat Methods =====
    
    /**
     * Applies damage to this slime.
     * 
     * @param damage Amount of damage to take
     */
    public void takeDamage(float damage) {
        if (isDead) return;
        
        currentHP -= damage;
        if (currentHP <= 0) {
            currentHP = 0;
            die();
        }
    }
    
    /**
     * Performs a basic attack and returns the damage value.
     * 
     * @return The attack damage (with ±10% random variance)
     */
    public float attack() {
        if (isDead) return 0f;
        
        // Add ±10% random variance
        float variance = 0.9f + (float)(Math.random() * 0.2f);
        return attackDamage * variance;
    }
    
    /**
     * Kills this slime.
     */
    public void die() {
        if (isDead) return;
        isDead = true;
        currentHP = 0;
    }
    
    // ===== Movement =====
    
    /**
     * Moves the slime by the specified delta.
     * 
     * @param dx Change in X position
     * @param dy Change in Y position
     */
    public void setPositionWorld(int x, int y) {
        this.pointsWorldX = x;
        this.pointsWorldY = y;
    }
    
    // ===== Getters =====

    public int getAttackCooldown() {
        return attackCooldown;
    }
    
    public boolean isDead() {
        return isDead;
    }
    
    
    public float getCurrentHP() {
        return currentHP;
    }
    
    public float getMaxHP() {
        return maxHP;
    }
    
    public float getAttackDamage() {
        return attackDamage;
    }
    
    public float getMoveSpeed() {
        return moveSpeed;
    }
    
    public float getAttackRange() {
        return attackRange;
    }
    
    public float getPositionX() {
        return positionX;
    }
    
    public float getPositionY() {
        return positionY;
    }
    public int getPointsWorldX() {
        return pointsWorldX;
    }
    
    public int getPointsWorldY() {
        return pointsWorldY;
    }
    
    public int getCurrentWave() {
        return currentWave;
    }
    
    public float getHPPercentage() {
        return maxHP > 0 ? (currentHP / maxHP) * 100f : 0f;
    }
    public float getSizeX() {
        return sizeX;
    }
    public float getSizeY() {
        return sizeY;
    }
    
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
    public long getLastDamageTime() {
        return lastDamageTime;
    }
    public void setLastDamageTime(long t) {
        lastDamageTime = t;
    }
}
