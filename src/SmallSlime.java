/**
 * Small slime - fast, ranged attacker.
 * 
 * Special features:
 * - Can attack from long range (300 pixels)
 * - Higher movement speed
 * - Lower HP and damage
 * - Ranged attacks have +15% bonus damage
 */
public class SmallSlime extends Slime {
    
    // ===== Special Stats =====
    private float rangedAttackBonus = 1.15f;  // +15% damage for ranged attacks
    private float rangedRange = 300f;
    
    // ===== Constructor =====
    
    /**
     * Creates a small slime at the specified level.
     * 
     * @param level The slime's level
     */
    public SmallSlime(int level) {
        super(level, SlimeSize.SMALL, SlimeType.RANGED);
    }
    
    // ===== Stat Calculation =====
    
    @Override
    protected void calculateBaseStats() {
        // Base stats are handled by Slime parent class
        // Size multipliers are automatically applied
    }
    
    // ===== Special Abilities =====
    
    /**
     * Performs a ranged attack with bonus damage.
     * 
     * @return Ranged attack damage (with bonus and ±10% variance)
     */
    public float performRangedAttack() {
        if (isDead) return 0f;
        
        // Base attack with ranged bonus
        float baseDamage = attackDamage * rangedAttackBonus;
        
        // Add ±10% random variance
        float variance = 0.9f + (float)(Math.random() * 0.2f);
        
        return baseDamage * variance;
    }
    
    /**
     * Checks if a target is within ranged attack range.
     * 
     * @param targetX Target X coordinate
     * @param targetY Target Y coordinate
     * @return true if target is within range
     */
    public boolean isInRangedRange(float targetX, float targetY) {
        return distanceTo(targetX, targetY) <= rangedRange;
    }
    
    /**
     * Gets the maximum ranged attack distance.
     * 
     * @return Ranged attack range in pixels
     */
    public float getRangedRange() {
        return rangedRange;
    }
}
