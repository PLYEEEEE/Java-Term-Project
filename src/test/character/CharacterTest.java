package test.character;

import characters.Character;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    // สร้างคลาสลูกปลอมไว้ใช้ test
    class TestCharacter extends Character {
        public TestCharacter() {
            super("Test", 100, 0, 0, 1.0f, 32, 32);
        }
    }

    @Test
    void testTakeDamageReducesHealth() {
        TestCharacter c = new TestCharacter();

        c.takeDamage(20);

        assertTrue(c.isAlive());
    }

    @Test
    void testCharacterDiesWhenHealthZero() {
        TestCharacter c = new TestCharacter();

        c.takeDamage(150);

        assertFalse(c.isAlive());
    }

    @Test
    void testPositionSetters() {
        TestCharacter c = new TestCharacter();

        c.setWorldX(50);
        c.setWorldY(80);

        assertEquals(50, c.getWorldX());
        assertEquals(80, c.getWorldY());
    }
}