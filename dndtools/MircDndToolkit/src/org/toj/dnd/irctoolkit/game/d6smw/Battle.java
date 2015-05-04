package org.toj.dnd.irctoolkit.game.d6smw;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.toj.dnd.irctoolkit.game.dnd3r.battle.event.InitiativePassesEvent;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Battle {
    private LinkedList<Unit> initArray = new LinkedList<Unit>();
    private Unit current;
    private int round = -1;

    public Battle() {
    }

    public void join(Unit u, int init) {
        u.setInit(init);
        initArray.add(u);
        sortMechsInBattle();
    }

    private void sortMechsInBattle() {
        Collections.sort(initArray, new Comparator<Unit>() {
            @Override
            public int compare(Unit m1, Unit m2) {
                return m2.getInit() - m1.getInit();
            }
        });
    }

    public void remove(String unitName) {
        Unit unit = getUnit(unitName);
        if (current == unit) {
            end();
        }
        initArray.remove(unit);
    }

    public void end() {
        if (current == null || initArray == null || initArray.isEmpty()) {
            return;
        }
        if (current == initArray.getLast()) {
            this.go(initArray.getFirst(), round + 1);
        } else {
            this.go(initArray.get(initArray.indexOf(current) + 1), round);
        }
    }

    public void startRound(int round) {
        go(initArray.getFirst(), round);
    }

    public void go(Unit u, int round) {
        // fire battle event
        if (round >= 0 && u != null && current != null) {
            fireInitiativeChange(round, u.getInit());
        }
        this.round = round;
        if (round < 0) {
            current = null;
        } else {
            current = u;
        }
    }

    private void fireInitiativeChange(int newRound, double newInit) {
        int oldRound = round;
        double oldInit = current.getInit();
        for (Unit u : initArray) {
            if (u.hasHeat()) {
                u.sinkHeat(calcInitPassesTimes(new InitiativePassesEvent(
                        oldRound, oldInit, newRound, newInit), u.getInit()));
            }
        }
    }

    private int calcInitPassesTimes(InitiativePassesEvent e, int init) {
        int triggeredTimes = e.getRound() - e.getCurrentRound();

        if (e.getCurrentInit() > init && e.getInit() <= init) {
            triggeredTimes++;
        }
        if (e.getCurrentInit() <= init && e.getInit() > init) {
            triggeredTimes--;
        }

        return triggeredTimes;
    }

    TimePoint getCurrentTimePoint() {
        TimePoint currentTp = null;
        if (round > 0 && current != null) {
            currentTp = new TimePoint(round, current.getInit());
        }
        return currentTp;
    }

    public String activateEquipment(Mech mech, String eq) {
        return mech.activateEquipment(eq, getCurrentTimePoint());
    }

    public Unit getUnit(String name) {
        for (Unit u : initArray) {
            if (AbbreviationUtil.isAbbre(name, u.getName())) {
                return u;
            }
        }
        return null;
    }

    String generateTopic() {
        StringBuilder sb = new StringBuilder();
        if (round < 0) {
            sb.append("setup: ");
        } else if (round == 0) {
            sb.append("surprise: ");
        } else {
            sb.append("r").append(round).append(": ");
        }

        for (Unit u : initArray) {
            if (!u.equals(initArray.getFirst())) {
                sb.append(", ");
            }
            sb.append(u.toTopicString(u.equals(current)));
        }
        return sb.toString();
    }

    public Unit getCurrent() {
        return current;
    }

    public void setCurrent(Unit current) {
        this.current = current;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
