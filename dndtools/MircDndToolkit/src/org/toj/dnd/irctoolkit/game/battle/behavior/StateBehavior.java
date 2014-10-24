package org.toj.dnd.irctoolkit.game.battle.behavior;

import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.event.BattleEvent;

public interface StateBehavior {
    String fireBattleEvent(BattleEvent event, Combatant owner);
}
