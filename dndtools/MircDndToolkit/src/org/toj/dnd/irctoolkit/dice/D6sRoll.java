package org.toj.dnd.irctoolkit.dice;

import java.util.ArrayList;
import java.util.List;

public class D6sRoll {
    private int diceNumber;
    private boolean autoSuccAfter15;

    private int baseSucc = 0;
    private int rolledSucc = 0;
    private List<Integer> diceRollResults;
    
    public D6sRoll(int number, boolean autoSuccAfter15) {
        this.diceNumber = number;
        this.autoSuccAfter15 = autoSuccAfter15;
    }

    public void roll() {
        int diceNumberToRoll = diceNumber;
        if (autoSuccAfter15 && diceNumber > 15) {
            baseSucc = diceNumber - 15;
            diceNumberToRoll = 15;
        }

        baseSucc += diceNumberToRoll / 3;
        diceNumberToRoll = diceNumber - baseSucc;
        diceRollResults = new ArrayList<Integer>(diceNumberToRoll);

        for (int i = 0; i < diceNumberToRoll; i++) {
            int roll = Dice.getDice(6).roll();
            diceRollResults.add(roll);
            if (roll > 3) {
                rolledSucc++;
            }
        }
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public boolean isAutoSuccAfter15() {
        return autoSuccAfter15;
    }

    public int getBaseSucc() {
        return baseSucc;
    }

    public int getRolledSucc() {
        return rolledSucc;
    }

    public List<Integer> getDiceRollResults() {
        return diceRollResults;
    }
}
