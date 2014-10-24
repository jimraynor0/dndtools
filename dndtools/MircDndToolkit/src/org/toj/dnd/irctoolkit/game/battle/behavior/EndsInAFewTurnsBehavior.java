package org.toj.dnd.irctoolkit.game.battle.behavior;

import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;
import org.toj.dnd.irctoolkit.game.battle.event.BattleEvent;
import org.toj.dnd.irctoolkit.game.battle.event.InitiativePassesEvent;

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
                    .append("]���ϵ�[").append(controllsState.getName())
                    .append("]Ч����ʧ�ˡ�").toString();
        }
        return null;
    }
}
