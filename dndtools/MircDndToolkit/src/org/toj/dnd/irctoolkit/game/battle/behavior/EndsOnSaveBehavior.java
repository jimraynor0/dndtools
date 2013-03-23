package org.toj.dnd.irctoolkit.game.battle.behavior;

import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;

public class EndsOnSaveBehavior implements StateBehavior {

    private State controllsState;

    public EndsOnSaveBehavior(State controlls) {
        this.controllsState = controlls;
    }

    @Override
    public String onTurnStart(int round, Combatant current, Combatant owner) {
        return null;
    }

    @Override
    public String onTurnEnd(int round, Combatant current, Combatant owner) {
        if (round >= this.controllsState.getAppliedOnRound()
                && current == owner) {
            return new StringBuilder("[").append(owner.getName())
                    .append("]需要为[").append(controllsState).append("]投save")
                    .toString();
        }
        return null;
    }
}
