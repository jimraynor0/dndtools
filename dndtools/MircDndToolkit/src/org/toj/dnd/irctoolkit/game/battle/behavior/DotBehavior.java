package org.toj.dnd.irctoolkit.game.battle.behavior;

import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;
import org.toj.dnd.irctoolkit.game.battle.event.BattleEvent;
import org.toj.dnd.irctoolkit.game.battle.event.InitiativePassesEvent;

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
                    .append("]�ܵ�����[").append(controllsState).append("]��[")
                    .append(dmg * triggered).append("]���˺�").toString();
        }
        return null;
    }

    private int calcTriggeredTimes(InitiativePassesEvent e) {
        int triggeredTimes = e.getRound() - e.getCurrentRound();

        if (e.getCurrentInit() > controllsState.getAppliedOnInit() && e.getInit() <= controllsState.getAppliedOnInit()) {
            triggeredTimes++;
        }
        if (e.getCurrentInit() <= controllsState.getAppliedOnInit() && e.getInit() > controllsState.getAppliedOnInit()) {
            triggeredTimes--;
        }

        return triggeredTimes;
    }
}
