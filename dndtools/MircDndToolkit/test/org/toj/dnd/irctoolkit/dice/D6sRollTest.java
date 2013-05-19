package org.toj.dnd.irctoolkit.dice;

import static org.junit.Assert.*;

import org.junit.Test;

public class D6sRollTest {

    @Test
    public void testRoll() {
        for (int i = 0; i < 100000; i++) {
            D6sRoll diceRoll = new D6sRoll(5, true);
            diceRoll.roll();
            assertEquals(1, diceRoll.getBaseSucc());
            assertTrue(diceRoll.getRolledSucc() <= 4);

            diceRoll = new D6sRoll(6, true);
            diceRoll.roll();
            assertEquals(2, diceRoll.getBaseSucc());
            assertTrue(diceRoll.getRolledSucc() <= 4);

            diceRoll = new D6sRoll(25, true);
            diceRoll.roll();
            assertEquals(15, diceRoll.getBaseSucc());
            assertTrue(diceRoll.getRolledSucc() <= 10);

            diceRoll = new D6sRoll(25, false);
            diceRoll.roll();
            assertEquals(8, diceRoll.getBaseSucc());
            assertTrue(diceRoll.getRolledSucc() <= 17);
        }
    }
}
