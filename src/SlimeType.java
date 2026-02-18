/**
 * Enum representing the attack type of a slime.
 */
public enum SlimeType {
    /** Close-range melee attacker */
    MELEE(60f),
    
    /** Long-range projectile attacker */
    RANGED(300f);

    private final float baseAttackRange;

    SlimeType(float baseAttackRange) {
        this.baseAttackRange = baseAttackRange;
    }

    public float getBaseAttackRange() {
        return baseAttackRange;
    }
}
