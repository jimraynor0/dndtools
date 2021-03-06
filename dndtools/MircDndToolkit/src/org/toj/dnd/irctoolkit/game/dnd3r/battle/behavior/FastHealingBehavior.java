package org.toj.dnd.irctoolkit.game.dnd3r.battle.behavior;

import org.toj.dnd.irctoolkit.game.dnd3r.battle.Combatant;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.State;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.event.BattleEvent;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.event.InitiativePassesEvent;

public class FastHealingBehavior implements StateBehavior {

    private int heal;
    private State controllsState;

    public FastHealingBehavior(int heal, State controlls) {
        this.heal = heal;
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
            owner.heal(heal * triggered);
            return new StringBuilder("[").append(owner.getName())
                    .append("]恢复了[").append(heal * triggered).append("]点hp")
                    .toString();
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

    public static void main(String[] args) {
        Combatant c = new Combatant("test");
        State state = State.parseState("fh2", 3, 15);
        c.applyState(state);
        FastHealingBehavior b = new FastHealingBehavior(2, state);
        System.out.println(b.calcTriggeredTimes(new InitiativePassesEvent(3,
                20, 3, 15)));
        System.out.println(b.calcTriggeredTimes(new InitiativePassesEvent(3,
                15, 3, 10)));
        System.out.println(b.calcTriggeredTimes(new InitiativePassesEvent(3,
                20, 3, 10)));
        System.out.println(b.calcTriggeredTimes(new InitiativePassesEvent(2,
                20, 4, 20)));
        System.out.println(b.calcTriggeredTimes(new InitiativePassesEvent(2,
                20, 4, 15)));
        System.out.println(b.calcTriggeredTimes(new InitiativePassesEvent(2,
                20, 4, 10)));
        System.out.println(b.calcTriggeredTimes(new InitiativePassesEvent(2,
                15, 4, 20)));
        System.out.println(b.calcTriggeredTimes(new InitiativePassesEvent(2,
                15, 4, 15)));
        System.out.println(b.calcTriggeredTimes(new InitiativePassesEvent(2,
                15, 4, 10)));
    }
}
