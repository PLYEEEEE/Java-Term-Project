package test.character;

import characters.Slime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlimeTest {

    @Test
    void slime_shouldStartAlive() {
        Slime s = new Slime(32, 32);

        assertFalse(s.isDead());
        assertEquals(s.getMaxHP(), s.getCurrentHP());
        assertEquals(1, s.getCurrentWave());
    }

    @Test
    void takeDamage_shouldReduceHP() {
        Slime s = new Slime(32, 32);

        float hpBefore = s.getCurrentHP();
        s.takeDamage(1f);

        assertTrue(s.getCurrentHP() < hpBefore);
    }

    @Test
    void takeDamage_shouldDieWhenHPZero() {
        Slime s = new Slime(32, 32);

        s.takeDamage(1000f);

        assertTrue(s.isDead());
        assertEquals(0f, s.getCurrentHP());
    }

    @Test
    void attack_shouldReturnDamage() {
        Slime s = new Slime(32, 32);

        float dmg = s.attack();

        assertTrue(dmg > 0);
    }

    @Test
    void attack_shouldReturnZeroWhenDead() {
        Slime s = new Slime(32, 32);
        s.takeDamage(999f);

        float dmg = s.attack();

        assertEquals(0f, dmg);
    }

    @Test
    void updateStatsForWave_shouldIncreaseStats() {
        Slime s = new Slime(32, 32);

        float oldHP = s.getMaxHP();
        float oldATK = s.getAttackDamage();

        s.updateStatsForWave(5);

        assertTrue(s.getMaxHP() > oldHP);
        assertTrue(s.getAttackDamage() > oldATK);
        assertEquals(5, s.getCurrentWave());
    }

    @Test
    void die_shouldSetDeadTrue() {
        Slime s = new Slime(32, 32);

        s.die();

        assertTrue(s.isDead());
        assertEquals(0f, s.getCurrentHP());
    }
}