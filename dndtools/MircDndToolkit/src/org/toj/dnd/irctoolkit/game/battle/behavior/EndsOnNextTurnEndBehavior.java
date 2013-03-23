package org.toj.dnd.irctoolkit.game.battle.behavior;

import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;

public class EndsOnNextTurnEndBehavior implements StateBehavior {

    private State controllsState;

    public EndsOnNextTurnEndBehavior(State controlls) {
        this.controllsState = controlls;
    }

    @Override
    public String onTurnStart(int round, Combatant current, Combatant owner) {
        if (round > this.controllsState.getAppliedOnRound() + 1
                || round == this.controllsState.getAppliedOnRound() + 1
                && current.getInit() < this.controllsState.getAppliedOnInit()) {
            owner.removeState(controllsState);
            return new StringBuilder("[").append(owner.getName())
                    .append("]身上的[").append(controllsState).append("]消失了")
                    .toString();
        }
        return null;
    }

    @Override
    public String onTurnEnd(int round, Combatant current, Combatant owner) {
        if (round > this.controllsState.getAppliedOnRound() + 1
                || round == this.controllsState.getAppliedOnRound() + 1
                && current.getInit() <= this.controllsState.getAppliedOnInit()) {
            owner.removeState(controllsState);
            return new StringBuilder("[").append(owner.getName())
                    .append("]身上的[").append(controllsState).append("]消失了")
                    .toString();
        }
        return null;
    }
}
