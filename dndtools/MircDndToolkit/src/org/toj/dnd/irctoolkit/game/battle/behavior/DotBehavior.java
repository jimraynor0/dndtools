package org.toj.dnd.irctoolkit.game.battle.behavior;

import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;

public class DotBehavior implements StateBehavior {

    private int dmg;
    private State controllsState;

    public DotBehavior(int dmg, State controlls) {
        this.dmg = dmg;
        this.controllsState = controlls;
    }

    @Override
    public String onTurnStart(int round, double init, Combatant owner) {
        if (round >= this.controllsState.getAppliedOnRound()
                && init == owner.getInit()) {
            owner.damage(dmg);
            return new StringBuilder("[").append(owner.getName())
                    .append("]受到来自[").append(controllsState).append("]的[")
                    .append(dmg).append("]点伤害").toString();
        }
        return null;
    }

    @Override
    public String onTurnEnd(int round, double init, Combatant owner) {
        return null;
    }
}
