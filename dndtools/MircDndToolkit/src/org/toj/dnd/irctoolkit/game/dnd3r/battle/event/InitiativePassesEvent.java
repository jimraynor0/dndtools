package org.toj.dnd.irctoolkit.game.dnd3r.battle.event;

public class InitiativePassesEvent extends BattleEvent {
    private double currentInit;
    private int currentRound;

    public InitiativePassesEvent(int currentRound, double currentInit,
            int newRound, double newInit) {
        super(newRound, newInit);
        this.currentInit = currentInit;
        this.currentRound = currentRound;
    }

    public double getCurrentInit() {
        return currentInit;
    }

    public void setCurrentInit(double currentInit) {
        this.currentInit = currentInit;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
}
