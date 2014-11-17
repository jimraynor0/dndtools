package org.toj.dnd.irctoolkit.dice;

import java.util.HashMap;
import java.util.Map;

public class Dice {
    private static final Map<Integer, Dice> DICE_POOL = new HashMap<Integer, Dice>();

    public static Dice getDice(int side) {
        if (!DICE_POOL.containsKey(side)) {
            Dice dice = new Dice();
            dice.side = side;
            DICE_POOL.put(side, dice);
        }
        return DICE_POOL.get(side);
    }

    private int side = 20;

    public int roll() {
        return (1 + (int) (Math.random() * side));
    }
}
