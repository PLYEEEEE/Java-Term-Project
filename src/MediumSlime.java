/**
 * Medium slime - balanced melee fighter.
 * 
 * Special features:
 * - Standard HP, damage, and speed
 * - Melee range attacker (60 pixels)
 * - Most common slime type in waves
 * - No special abilities, reliable stats
 */
public class MediumSlime extends Slime {
    
    // ===== Special Stats =====
    private float meleeBonus = 1.0f;  // No special bonus, standard damage
    
    // ===== Constructor =====
    
    /**
     * Creates a medium slime at the specified level.
     * 
     * @param level The slime's level
     */
    public MediumSlime(int level) {
        super(level, SlimeSize.MEDIUM, SlimeType.MELEE);
    }
    
    // ===== Stat Calculation =====
    
    @Override
    protected void calculateBaseStats() {
        // Base stats are handled by Slime parent class
        // MEDIUM size has 1.0x multipliers (standard)
    }
    
    // ===== Special Methods =====
    
    /**
     * Performs a standard melee attack.
     * Medium slimes use the base attack() method from parent.
     * 
     * @return Attack damage with standard variance
     */
    @Override
    public float attack() {
        // Use parent's attack method (includes ±10% variance)
        return super.attack();
    }
    
    /**
     * Checks if target is within melee range.
     * 
     * @param targetX Target X coordinate
     * @param targetY Target Y coordinate
     * @return true if target is within melee range
     */
    public boolean isInMeleeRange(float targetX, float targetY) {
        return distanceTo(targetX, targetY) <= attackRange;
    }
}
