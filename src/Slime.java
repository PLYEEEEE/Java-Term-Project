/**
 * Concrete class for slime monsters.
 * Provides core functionality for HP, damage, movement.
 * 
 * Stats scale infinitely with wave number:
 * - HP increases by 10% per wave
 * - Damage increases by 8% per wave
 */
public class Slime {
    
    // ===== Base Stats (before multipliers) =====
    private static final float BASE_HP = 100f;
    private static final float BASE_ATTACK = 15f;
    private static final float BASE_MOVE_SPEED = 80f;
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
    protected boolean isDead;
    
    // Current wave number (for stat scaling)
    protected int currentWave;
    
    // ===== Constructor =====
    
    /**
     * Creates a new slime. Uses default medium size and melee type.
     */
    public Slime() {
        this.currentWave = 1;
        this.positionX = 0f;
        this.positionY = 0f;
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
    public void move(float dx, float dy) {
        if (isDead) return;
        this.positionX += dx;
        this.positionY += dy;
    }
    
    /**
     * Calculates distance to a target position.
     * 
     * @param targetX Target X coordinate
     * @param targetY Target Y coordinate
     * @return Distance in pixels
     */
    public float distanceTo(float targetX, float targetY) {
        float dx = targetX - positionX;
        float dy = targetY - positionY;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }
    
    // ===== Getters =====
    
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
    
    public int getCurrentWave() {
        return currentWave;
    }
    
    public float getHPPercentage() {
        return maxHP > 0 ? (currentHP / maxHP) * 100f : 0f;
    }
    
    // ===== Setters =====
    
    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
    }
    
    @Override
    public String toString() {
        return String.format("%s[Wave%d | HP:%.0f/%.0f ATK:%.1f SPD:%.1f]",
            getClass().getSimpleName(), currentWave,
            currentHP, maxHP, attackDamage, moveSpeed);
    }
}
