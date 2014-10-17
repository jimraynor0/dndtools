package org.toj.dnd.irctoolkit.game.battle.behavior;

public class BattleEvent {
    private int round;
    private double init;

    public BattleEvent(int newRound, double newInit) {
        this.round = newRound;
        this.init = newInit;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public double getInit() {
        return init;
    }

    public void setInit(double init) {
        this.init = init;
    }
}
