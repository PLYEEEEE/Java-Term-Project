/**
 * Large boss slime - powerful tank with special stomp ability.
 * 
 * Special features:
 * - Very high HP (3.5x multiplier)
 * - High damage (2.5x multiplier)
 * - Slower movement (0.7x speed)
 * - Boss-tier exp reward
 * - Special "stomp" ability triggers at 50% HP (spawns 2 small slimes)
 */
public class LargeSlime extends Slime {
    
    // ===== Boss Stats =====
    private float bossHPMultiplier = 3.5f;
    private float bossDamageMultiplier = 2.5f;
    
    // ===== Stomp Ability =====
    private boolean stompUsed = false;
    private float stompThreshold = 0.50f;  // Triggers at 50% HP
    
    // ===== Constructor =====
    
    /**
     * Creates a large boss slime at the specified level.
     * 
     * @param level The slime's level
     */
    public LargeSlime(int level) {
        super(level, SlimeSize.LARGE_BOSS, SlimeType.MELEE);
    }
    
    // ===== Stat Calculation =====
    
    @Override
    protected void calculateBaseStats() {
        // Base stats are handled by Slime parent class
        // LARGE_BOSS size already has high multipliers
        // Additional boss-specific calculations done here if needed
    }
    
    // ===== Damage Override =====
    
    /**
     * Override takeDamage to check for stomp trigger.
     * 
     * @param damage Amount of damage to take
     */
    @Override
    public void takeDamage(float damage) {
        super.takeDamage(damage);
        
        // Check if stomp should trigger after taking damage
        if (!isDead) {
            checkStompTrigger();
        }
    }
    
    // ===== Special Abilities =====
    
    /**
     * Checks if the boss should trigger its stomp ability.
     * Stomp triggers once when HP drops to or below 50%.
     * 
     * @return true if stomp was triggered this check
     */
    public boolean checkStompTrigger() {
        if (stompUsed || isDead) {
            return false;
        }
        
        float hpRatio = currentHP / maxHP;
        
        if (hpRatio <= stompThreshold) {
            performStomp();
            return true;
        }
        
        return false;
    }
    
    /**
     * Performs the stomp ability.
     * This should spawn 2 SmallSlime minions near the boss.
     * 
     * Note: Actual minion spawning should be handled by the game's
     * monster spawner system using this trigger.
     */
    public void performStomp() {
        if (stompUsed) return;
        
        stompUsed = true;
        System.out.println("[BOSS STOMP] Large slime at " + getHPPercentage() + "% HP!");
        System.out.println("  → Spawning 2 small slime minions!");
        
        // In actual game, this would trigger:
        // spawner.spawnMinionsNear(this.positionX, this.positionY, 2);
    }
    
    /**
     * Performs a powerful boss melee attack with extra damage.
     * 
     * @return Boss attack damage (higher than normal)
     */
    @Override
    public float attack() {
        if (isDead) return 0f;
        
        // Boss attacks hit harder (already in stats, but add extra variance)
        float baseDamage = attackDamage;
        
        // Boss has wider damage variance (±15% instead of ±10%)
        float variance = 0.85f + (float)(Math.random() * 0.3f);
        
        return baseDamage * variance;
    }
    
    // ===== Getters =====
    
    /**
     * Checks if the boss has already used its stomp ability.
     * 
     * @return true if stomp was used
     */
    public boolean isStompUsed() {
        return stompUsed;
    }
    
    /**
     * Gets the HP threshold percentage for stomp trigger.
     * 
     * @return Stomp threshold (0.5 = 50%)
     */
    public float getStompThreshold() {
        return stompThreshold;
    }
    
    /**
     * Checks if boss is below stomp threshold.
     * 
     * @return true if HP is at or below stomp threshold
     */
    public boolean isBelowStompThreshold() {
        return (currentHP / maxHP) <= stompThreshold;
    }
}