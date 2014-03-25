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
    
    public static void main(String[] args) {
        D6sRoll jimSword = new D6sRoll(14, true);
        D6sRoll jimDodge = new D6sRoll(5, true);
        D6sRoll jimParry = new D6sRoll(16, true);
        D6sRoll wqSword = new D6sRoll(13, true);
        D6sRoll wqDodge = new D6sRoll(8, true);
        D6sRoll wqParry = new D6sRoll(13, true);
        
        jimSword.roll();
        System.out.println("JimÕ∂÷¿π•ª˜: " + (jimSword.getBaseSucc() + jimSword.getRolledSucc()));
        jimDodge.roll();
        System.out.println("JimÕ∂÷¿…¡±‹: " + (jimDodge.getBaseSucc() + jimDodge.getRolledSucc()));
        jimParry.roll();
        System.out.println("JimÕ∂÷¿∏Òµ≤: " + (jimParry.getBaseSucc() + jimParry.getRolledSucc()));
        wqSword.roll();
        System.out.println("JesÕ∂÷¿π•ª˜: " + (wqSword.getBaseSucc() + wqSword.getRolledSucc()));
        wqDodge.roll();
        System.out.println("JesÕ∂÷¿…¡±‹: " + (wqDodge.getBaseSucc() + wqDodge.getRolledSucc()));
        wqParry.roll();
        System.out.println("JesÕ∂÷¿∏Òµ≤: " + (wqParry.getBaseSucc() + wqParry.getRolledSucc()));
    }
}
