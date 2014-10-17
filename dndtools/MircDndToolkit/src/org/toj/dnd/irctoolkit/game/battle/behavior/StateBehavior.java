package org.toj.dnd.irctoolkit.game.battle.behavior;

import org.toj.dnd.irctoolkit.game.battle.Combatant;

public interface StateBehavior {

    String onTurnStart(int round, double init, Combatant owner);

    String onTurnEnd(int round, double init, Combatant owner);
}
