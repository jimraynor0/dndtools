package org.toj.dnd.irctoolkit.game.battle.behavior;

import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;

public class EndsOnNextTurnStartBehavior implements StateBehavior {

    private State controllsState;

    public EndsOnNextTurnStartBehavior(State controlls) {
        this.controllsState = controlls;
    }

    @Override
    public String onTurnStart(int round, Combatant current, Combatant owner) {
        if (round > this.controllsState.getAppliedOnRound() + 1
                || round == this.controllsState.getAppliedOnRound() + 1
                && current.getInit() <= this.controllsState.getAppliedOnInit()) {
            owner.removeState(controllsState);
            return new StringBuilder("[").append(controllsState)
                    .append("] faded from [").append(owner.getName())
                    .append("]").toString();
        }
        return null;
    }

    @Override
    public String onTurnEnd(int round, Combatant current, Combatant owner) {
        return null;
    }
}
