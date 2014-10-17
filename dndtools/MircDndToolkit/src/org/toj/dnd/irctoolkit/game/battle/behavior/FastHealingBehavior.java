package org.toj.dnd.irctoolkit.game.battle.behavior;

import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;

public class FastHealingBehavior implements StateBehavior {

    private int heal;
    private State controllsState;

    public FastHealingBehavior(int heal, State controlls) {
        this.heal = heal;
        this.controllsState = controlls;
    }

    @Override
    public String onTurnStart(int round, double init, Combatant owner) {
        if (round >= this.controllsState.getAppliedOnRound()
                && init == owner.getInit()) {
            owner.heal(heal);
            return new StringBuilder("[").append(owner.getName())
                    .append("]ª÷∏¥¡À[").append(heal).append("]µ„hp").toString();
        }
        return null;
    }

    @Override
    public String onTurnEnd(int round, double init, Combatant owner) {
        return null;
    }
}
