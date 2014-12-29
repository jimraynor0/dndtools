package org.toj.dnd.irctoolkit.game.dnd3r.battle.behavior;

import org.toj.dnd.irctoolkit.game.dnd3r.battle.Combatant;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.event.BattleEvent;

public interface StateBehavior {
    String fireBattleEvent(BattleEvent event, Combatant owner);
}
