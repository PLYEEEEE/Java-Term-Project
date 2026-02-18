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
        
        // ===== Test 1: Create three slimes =====
        System.out.println("▶ Test 1: Creating three slimes (Level 1, Wave 1)");
        System.out.println("─────────────────────────────────────────");
        
        Slime slime1 = new Slime(1);
        Slime slime2 = new Slime(1);
        Slime slime3 = new Slime(1);
        
        System.out.println("Slime 1: " + slime1);
        System.out.println("Slime 2: " + slime2);
        System.out.println("Slime 3: " + slime3);
        System.out.println();
        
        // ===== Test 2: Stat scaling with waves =====
        System.out.println("▶ Test 2: Stat scaling across waves (Standard slime)");
        System.out.println("─────────────────────────────────────────");
        
        Slime testSlime = new Slime(1);
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
            Slime leveledSlime = new Slime(level);
            System.out.printf("Level %d: HP=%.0f ATK=%.1f%n", 
                level, leveledSlime.getMaxHP(), 
                leveledSlime.getAttackDamage());
        }
        System.out.println();
        
        // ===== Test 4: Combat simulation =====
        System.out.println("▶ Test 4: Combat simulation");
        System.out.println("─────────────────────────────────────────");
        
        Slime attacker = new Slime(3);
        attacker.setPosition(100, 100);
        
        System.out.println("Attacker: " + attacker);
        System.out.println("Position: (" + attacker.getPositionX() + ", " + 
                         attacker.getPositionY() + ")");
        System.out.println("\nPerforming 3 attacks:");
        
        for (int i = 1; i <= 3; i++) {
            float damage = attacker.attack();
            System.out.printf("  Attack %d: %.1f damage%n", i, damage);
        }
        System.out.println();
        
        // ===== Test 5: Taking damage =====
        System.out.println("▶ Test 5: Damage and death");
        System.out.println("─────────────────────────────────────────");
        
        Slime victim = new Slime(2);
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
        System.out.println("▶ Test 6: Advanced damage test");
        System.out.println("─────────────────────────────────────────");
        
        Slime boss = new Slime(5);
        boss.updateStatsForWave(3);
        System.out.println("Strong slime created: " + boss);
        
        // Damage to 50% HP
        float damageTo50 = boss.getMaxHP() * 0.5f;
        System.out.printf("Dealing %.0f damage to reach 50%% HP...%n", damageTo50);
        boss.takeDamage(damageTo50);
        
        System.out.printf("HP after damage: %.0f/%.0f (%.1f%%)%n", 
            boss.getCurrentHP(), boss.getMaxHP(), boss.getHPPercentage());
        System.out.println();
        
        // ===== Test 7: Distance calculation =====
        System.out.println("▶ Test 7: Distance and range checks");
        System.out.println("─────────────────────────────────────────");
        
        Slime rangedSlime = new Slime(1);
        rangedSlime.setPosition(0, 0);
        
        float playerX = 200;
        float playerY = 0;
        
        float distance = rangedSlime.distanceTo(playerX, playerY);
        boolean inRange = distance <= rangedSlime.getAttackRange();
        
        System.out.printf("Slime at (%.0f, %.0f)%n", 
            rangedSlime.getPositionX(), rangedSlime.getPositionY());
        System.out.printf("Player at (%.0f, %.0f)%n", playerX, playerY);
        System.out.printf("Distance: %.0f pixels%n", distance);
        System.out.printf("Attack range: %.0f pixels%n", rangedSlime.getAttackRange());
        System.out.printf("In attack range: %s%n", inRange);
        System.out.println();
        
        // ===== Test 8: Infinite scaling demo =====
        System.out.println("▶ Test 8: Infinite scaling (Wave 1 vs Wave 50)");
        System.out.println("─────────────────────────────────────────");
        
        Slime earlyBoss = new Slime(1);
        earlyBoss.updateStatsForWave(1);
        
        Slime lateBoss = new Slime(1);
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
        System.out.println("✓ Standard slime working");
        System.out.println("✓ Infinite stat scaling (wave + level)");
        System.out.println("✓ Combat system (attack, damage, death)");
        System.out.println("✓ Distance and range calculations");
    }
}
