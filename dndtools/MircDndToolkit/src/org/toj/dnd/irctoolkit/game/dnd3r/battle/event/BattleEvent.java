package org.toj.dnd.irctoolkit.game.dnd3r.battle.event;

public class BattleEvent {
    private int round;
    private double init;

    public BattleEvent(int round, double init) {
        this.round = round;
        this.init = init;
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
