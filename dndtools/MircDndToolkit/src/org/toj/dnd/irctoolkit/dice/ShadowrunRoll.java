package org.toj.dnd.irctoolkit.dice;

import java.util.ArrayList;
import java.util.List;

public class ShadowrunRoll {
    private int diceNumber;
    private int limit;

    private int succ;

    private List<Integer> diceRollResults;

    public ShadowrunRoll(int number, int limit) {
        this.diceNumber = number;
        this.limit = limit;
    }

    public void roll() {
        int diceNumberToRoll = diceNumber;

        diceRollResults = new ArrayList<>(diceNumberToRoll);

        for (int i = 0; i < diceNumberToRoll; i++) {
            int roll = Dice.getDice(6).roll();
            diceRollResults.add(roll);
            if (roll >= 5) {
                succ++;
            }
        }

        if (limit > 0 && succ > limit) {
            succ = limit;
        }
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public int getSucc() {
        return succ;
    }

    public List<Integer> getDiceRollResults() {
        return diceRollResults;
    }
}
