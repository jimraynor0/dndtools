package org.toj.dnd.irctoolkit.dice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FateRoll {
    private int resultNumber = 0;

    private List<String> diceRollResults;

    public void roll() {
        diceRollResults = new ArrayList<String>(4);

        for (int i = 0; i < 4; i++) {
            int roll = Dice.getDice(6).roll();
            if (roll < 3) {
                diceRollResults.add("-");
                resultNumber--;
            } else if (roll > 4) {
                diceRollResults.add("+");
                resultNumber++;
            } else {
                diceRollResults.add("o");
            }
            Collections.sort(diceRollResults);
        }
    }

    public List<String> getDiceResults() {
        return diceRollResults;
    }

    public int getResultNumber() {
        return resultNumber;
    }
}
