package org.toj.dnd.irctoolkit.game.dnd3r.battle.behavior;

import org.toj.dnd.irctoolkit.game.dnd3r.battle.Combatant;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.State;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.event.BattleEvent;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.event.InitiativePassesEvent;

public class DotBehavior implements StateBehavior {

    private int dmg;
    private State controllsState;

    public DotBehavior(int dmg, State controlls) {
        this.dmg = dmg;
        this.controllsState = controlls;
    }

    @Override
    public String fireBattleEvent(BattleEvent event, Combatant owner) {
        if (!(event instanceof InitiativePassesEvent)) {
            return null;
        }
        InitiativePassesEvent e = (InitiativePassesEvent) event;

        // figure out how many times fast healing has been triggered
        int triggered = calcTriggeredTimes(e);

        if (triggered > 0) {
            owner.damage(dmg * triggered);
            return new StringBuilder("[").append(owner.getName())
                    .append("]受到来自[").append(controllsState).append("]的[")
                    .append(dmg * triggered).append("]点伤害").toString();
        }
        return null;
    }

    private int calcTriggeredTimes(InitiativePassesEvent e) {
        int triggeredTimes = e.getRound() - e.getCurrentRound();

        if (e.getCurrentInit() > controllsState.getAppliedOnInit()
                && e.getInit() <= controllsState.getAppliedOnInit()) {
            triggeredTimes++;
        }
        if (e.getCurrentInit() <= controllsState.getAppliedOnInit()
                && e.getInit() > controllsState.getAppliedOnInit()) {
            triggeredTimes--;
        }

        return triggeredTimes;
    }
}
