package org.toj.dnd.irctoolkit.game.battle.behavior;

public class BattleEvent {
    private int turn;
    private double init;

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public double getInit() {
        return init;
    }

    public void setInit(double init) {
        this.init = init;
    }
}
