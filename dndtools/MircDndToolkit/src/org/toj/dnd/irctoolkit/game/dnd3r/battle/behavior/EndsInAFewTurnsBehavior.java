package org.toj.dnd.irctoolkit.game.dnd3r.battle.behavior;

import org.toj.dnd.irctoolkit.game.dnd3r.battle.Combatant;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.State;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.event.BattleEvent;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.event.InitiativePassesEvent;

public class EndsInAFewTurnsBehavior implements StateBehavior {

    private int endOnRound;
    private double endOnInit;
    private State controllsState;

    public EndsInAFewTurnsBehavior(State controlls) {
        this.controllsState = controlls;
        this.endOnRound = controllsState.getAppliedOnRound()
                + Integer.parseInt(this.controllsState.getEndCondition());
        this.endOnInit = controllsState.getAppliedOnInit();
    }

    @Override
    public String fireBattleEvent(BattleEvent event, Combatant owner) {
        if (!(event instanceof InitiativePassesEvent)) {
            return null;
        }
        InitiativePassesEvent e = (InitiativePassesEvent) event;

        // figure out how many rounds were left
        int roundsLeft = endOnRound - e.getRound();
        if (endOnInit < e.getInit()) {
            roundsLeft++;
        }

        // set as end condition string.
        this.controllsState.setEndCondition(String.valueOf(roundsLeft));

        if (roundsLeft <= 0) {
            owner.removeState(controllsState);
            return new StringBuilder("[").append(owner.getName())
                    .append("]身上的[").append(controllsState.getName())
                    .append("]效果消失了。").toString();
        }
        return null;
    }
}
