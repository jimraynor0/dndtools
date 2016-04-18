package org.toj.dnd.irctoolkit.dice;

import java.util.ArrayList;
import java.util.List;

public class WushuRoll {
    private int diceNumber;
    private int threshold;

    private int succ;

    private List<Integer> diceRollResults;

    public WushuRoll(int number, int threshold) {
        this.diceNumber = number;
        this.threshold = threshold;
    }

    public void roll() {
        int diceNumberToRoll = diceNumber;

        diceRollResults = new ArrayList<Integer>(diceNumberToRoll);

        for (int i = 0; i < diceNumberToRoll; i++) {
            int roll = Dice.getDice(6).roll();
            diceRollResults.add(roll);
            if (roll <= threshold) {
                succ++;
            }
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
