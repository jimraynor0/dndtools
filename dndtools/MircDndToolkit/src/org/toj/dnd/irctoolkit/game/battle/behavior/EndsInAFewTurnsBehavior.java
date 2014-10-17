package org.toj.dnd.irctoolkit.game.battle.behavior;

import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;

public class EndsInAFewTurnsBehavior implements StateBehavior {

    private int endOnRound;
    private State controllsState;

    public EndsInAFewTurnsBehavior(State controlls) {
        this.controllsState = controlls;
        this.endOnRound =
            controllsState.getAppliedOnRound()
                + Integer.parseInt(this.controllsState.getEndCondition());
    }

    @Override
    public String onTurnStart(int round, double init, Combatant owner) {
        int roundsLeft = Integer.parseInt(this.controllsState.getEndCondition());

        if ((endOnRound < roundsLeft + round) && init <= this.controllsState.getAppliedOnInit()) {
            roundsLeft--;
            this.controllsState.setEndCondition(String.valueOf(roundsLeft));
        } else if (endOnRound < roundsLeft + round - 1) {
            roundsLeft -= roundsLeft + round - endOnRound - 1;
            this.controllsState.setEndCondition(String.valueOf(roundsLeft));
        }

        if (roundsLeft <= 0) {
            owner.removeState(controllsState);
            return new StringBuilder("[").append(owner.getName())
                .append("]身上的[").append(controllsState.getName()).append("]效果消失了。")
                .toString();
        }
        return null;
    }

    @Override
    public String onTurnEnd(int round, double init, Combatant owner) {
        return null;
    }
}
