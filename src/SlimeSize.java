/**
 * Enum representing the size category of a slime.
 * Each size has different stat multipliers.
 */
public enum SlimeSize {
    /** Small slime - low HP, high speed, ranged attacker */
    SMALL(0.6f, 0.7f, 1.5f, 30f),
    
    /** Medium slime - balanced stats, standard melee */
    MEDIUM(1.0f, 1.0f, 1.0f, 50f),
    
    /** Large boss slime - very high HP and damage, slower */
    LARGE_BOSS(3.5f, 2.5f, 0.7f, 100f);

    private final float hpMultiplier;
    private final float damageMultiplier;
    private final float speedMultiplier;
    private final float sizeRadius;

    SlimeSize(float hpMultiplier, float damageMultiplier, 
              float speedMultiplier, float sizeRadius) {
        this.hpMultiplier = hpMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.speedMultiplier = speedMultiplier;
        this.sizeRadius = sizeRadius;
    }

    public float getHPMultiplier() {
        return hpMultiplier;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public float getSizeRadius() {
        return sizeRadius;
    }
}
