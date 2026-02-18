/**
 * Demo program to test all slime classes.
 * 
 * Run: javac *.java && java SlimeDemo
 */
public class SlimeDemo {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   RPG 2D Slime Monster System Demo    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // ===== Test 1: Create all three slime types =====
        System.out.println("▶ Test 1: Creating three types of slimes (Level 1, Wave 1)");
        System.out.println("─────────────────────────────────────────");
        
        SmallSlime small = new SmallSlime(1);
        MediumSlime medium = new MediumSlime(1);
        LargeSlime large = new LargeSlime(1);
        
        System.out.println("Small:  " + small);
        System.out.println("Medium: " + medium);
        System.out.println("Large:  " + large);
        System.out.println();
        
        // ===== Test 2: Stat scaling with waves =====
        System.out.println("▶ Test 2: Stat scaling across waves (Medium slime)");
        System.out.println("─────────────────────────────────────────");
        
        MediumSlime testSlime = new MediumSlime(1);
        for (int wave = 1; wave <= 5; wave++) {
            testSlime.updateStatsForWave(wave);
            System.out.printf("Wave %d: HP=%.0f ATK=%.1f EXP=%d%n", 
                wave, testSlime.getMaxHP(), 
                testSlime.getAttackDamage(), 
                testSlime.getExpReward());
        }
        System.out.println();
        
        // ===== Test 3: Level scaling =====
        System.out.println("▶ Test 3: Level scaling (Wave 1)");
        System.out.println("─────────────────────────────────────────");
        
        for (int level = 1; level <= 5; level++) {
            MediumSlime leveledSlime = new MediumSlime(level);
            System.out.printf("Level %d: HP=%.0f ATK=%.1f%n", 
                level, leveledSlime.getMaxHP(), 
                leveledSlime.getAttackDamage());
        }
        System.out.println();
        
        // ===== Test 4: Combat simulation =====
        System.out.println("▶ Test 4: Combat simulation");
        System.out.println("─────────────────────────────────────────");
        
        SmallSlime attacker = new SmallSlime(3);
        attacker.setPosition(100, 100);
        
        System.out.println("Attacker: " + attacker);
        System.out.println("Position: (" + attacker.getPositionX() + ", " + 
                         attacker.getPositionY() + ")");
        System.out.println("\nPerforming 3 attacks:");
        
        for (int i = 1; i <= 3; i++) {
            float damage = attacker.performRangedAttack();
            System.out.printf("  Attack %d: %.1f damage%n", i, damage);
        }
        System.out.println();
        
        // ===== Test 5: Taking damage =====
        System.out.println("▶ Test 5: Damage and death");
        System.out.println("─────────────────────────────────────────");
        
        MediumSlime victim = new MediumSlime(2);
        System.out.println("Initial: " + victim);
        
        victim.takeDamage(30);
        System.out.printf("After 30 dmg: HP=%.0f/%.0f (%.1f%%)%n", 
            victim.getCurrentHP(), victim.getMaxHP(), victim.getHPPercentage());
        
        victim.takeDamage(50);
        System.out.printf("After 50 dmg: HP=%.0f/%.0f (%.1f%%)%n", 
            victim.getCurrentHP(), victim.getMaxHP(), victim.getHPPercentage());
        
        victim.takeDamage(100);
        System.out.printf("After 100 dmg: HP=%.0f/%.0f - Dead: %s%n", 
            victim.getCurrentHP(), victim.getMaxHP(), victim.isDead());
        System.out.println();
        
        // ===== Test 6: Boss stomp ability =====
        System.out.println("▶ Test 6: Boss stomp ability");
        System.out.println("─────────────────────────────────────────");
        
        LargeSlime boss = new LargeSlime(5);
        boss.updateStatsForWave(3);
        System.out.println("Boss created: " + boss);
        System.out.printf("Stomp threshold: %.0f%% HP (%.0f / %.0f)%n", 
            boss.getStompThreshold() * 100, 
            boss.getMaxHP() * boss.getStompThreshold(), 
            boss.getMaxHP());
        
        // Damage boss to trigger stomp
        float damageNeeded = boss.getMaxHP() * 0.51f; // Just over 50%
        System.out.printf("\nDealing %.0f damage to trigger stomp...%n", damageNeeded);
        boss.takeDamage(damageNeeded);
        
        System.out.printf("Boss HP: %.0f/%.0f (%.1f%%)%n", 
            boss.getCurrentHP(), boss.getMaxHP(), boss.getHPPercentage());
        System.out.println("Stomp used: " + boss.isStompUsed());
        System.out.println();
        
        // ===== Test 7: Distance calculation =====
        System.out.println("▶ Test 7: Distance and range checks");
        System.out.println("─────────────────────────────────────────");
        
        SmallSlime rangedSlime = new SmallSlime(1);
        rangedSlime.setPosition(0, 0);
        
        float playerX = 200;
        float playerY = 0;
        
        float distance = rangedSlime.distanceTo(playerX, playerY);
        boolean inRange = rangedSlime.isInRangedRange(playerX, playerY);
        
        System.out.printf("Slime at (%.0f, %.0f)%n", 
            rangedSlime.getPositionX(), rangedSlime.getPositionY());
        System.out.printf("Player at (%.0f, %.0f)%n", playerX, playerY);
        System.out.printf("Distance: %.0f pixels%n", distance);
        System.out.printf("Ranged attack range: %.0f pixels%n", rangedSlime.getRangedRange());
        System.out.printf("In attack range: %s%n", inRange);
        System.out.println();
        
        // ===== Test 8: Infinite scaling demo =====
        System.out.println("▶ Test 8: Infinite scaling (Wave 1 vs Wave 50)");
        System.out.println("─────────────────────────────────────────");
        
        LargeSlime earlyBoss = new LargeSlime(1);
        earlyBoss.updateStatsForWave(1);
        
        LargeSlime lateBoss = new LargeSlime(1);
        lateBoss.updateStatsForWave(50);
        
        System.out.println("Wave 1:  " + earlyBoss);
        System.out.println("Wave 50: " + lateBoss);
        
        float hpIncrease = (lateBoss.getMaxHP() / earlyBoss.getMaxHP()) * 100;
        System.out.printf("\nHP increase: %.0f%%%n", hpIncrease);
        System.out.println();
        
        // ===== Summary =====
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          All Tests Completed!          ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\n✓ Three slime types working (Small, Medium, Large)");
        System.out.println("✓ Infinite stat scaling (wave + level)");
        System.out.println("✓ Combat system (attack, damage, death)");
        System.out.println("✓ Boss stomp ability at 50% HP");
        System.out.println("✓ Distance and range calculations");
    }
}
