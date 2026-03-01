package test.character;

import characters.Knight;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KnightTest {

    @Test
    void attack_shouldSetAttackingTrue() {
        Knight k = new Knight("Test",0,0,32,32,800,600,null);

        k.attack();

        assertTrue(k.attacking);
    }

    @Test
    void useSkill_shouldSetUsingSkillTrue() {
        Knight k = new Knight("Test",0,0,32,32,800,600,null);

        k.useSkill();

        assertTrue(k.usingSkill);
    }

    @Test
    void takeDamage_shouldReduceHealth() {
        Knight k = new Knight("Test",0,0,32,32,800,600,null);

        k.takeDamage(3);

        assertEquals(7, k.getHealth());
    }

    @Test
    void takeDamage_shouldNotGoBelowZero() {
        Knight k = new Knight("Test",0,0,32,32,800,600,null);

        k.takeDamage(50);

        assertEquals(0, k.getHealth());
    }

    @Test
    void setMove_shouldChangeState() {
        Knight k = new Knight("Test",0,0,32,32,800,600,null);

        k.setMove(true);

        assertTrue(k.getMove());
    }
}