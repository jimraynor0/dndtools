package org.toj.dnd.irctoolkit.game.dnd3r.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.toj.dnd.irctoolkit.game.dnd3r.PC;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.event.InitiativePassesEvent;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Battle implements Cloneable {

    private LinkedList<Combatant> combatants;
    private Combatant current;
    private int round = -1;
    private List<String> eventResultBuffer = new LinkedList<String>();

    public Battle() {
        this.combatants = new LinkedList<Combatant>();
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        for (Combatant c : this.combatants) {
            if (c.equals(current)) {
                current = c;
            }
        }
    }

    public void addChar(String charName) {
        double init = 0;
        if (!combatants.isEmpty()) {
            init = combatants.getLast().getInit() - 1;
        }
        combatants.add(new Combatant(charName, init));
    }

    public void addCharByInit(String charName, double init) {
        init = findNextNonoccupiedInit(init);
        Combatant ch = this.findCharByNameOrAbbre(charName);
        if (ch == null) {
            combatants.add(new Combatant(charName, init));
        } else {
            ch.setInit(init);
        }
        restoreOrder();
    }

    public void addChar(PC pc) {
        double init = 0;
        if (!combatants.isEmpty()) {
            init = combatants.getLast().getInit() - 1;
        }
        pc.setInit(init);

        Combatant ch = this.findCharByNameOrAbbre(pc.getName());
        if (ch == null) {
            combatants.add(pc);
        }
    }

    public void addCharByInit(PC pc, double init) {
        init = findNextNonoccupiedInit(init);
        pc.setInit(init);
        Combatant ch = this.findCharByNameOrAbbre(pc.getName());
        if (ch == null) {
            combatants.add(pc);
        }
        restoreOrder();
    }

    private double findNextNonoccupiedInit(double init) {
        for (Combatant c : combatants) {
            if (c.getInit() == init) {
                init -= 0.03;
            }
        }
        return init;
    }

    public void startRound(int n) {
        if (combatants != null && !combatants.isEmpty()) {
            this.go(combatants.getFirst(), n);
        } else {
            this.go((Combatant) null, n);
        }
    }

    public void go(String charName) {
        this.go(charName, round);
    }

    public void go(String charName, int round) {
        this.go(findCharByNameOrAbbre(charName), round);
    }

    public void go(Combatant c, int round) {
        if (round >= 0 && c != null && current != null) {
            fireInitiativeChange(round, c.getInit());
        }
        // fire battle event
        this.round = round;
        if (round < 0) {
            current = null;
        } else {
            current = c;
        }
    }

    public void pre() {
        if (current == null || combatants == null || combatants.isEmpty()) {
            return;
        }
        if (current.equals(combatants.getFirst())) {
            this.go(combatants.getLast(), round - 1);
        } else {
            this.go(combatants.get(combatants.indexOf(current) - 1), round);
        }
    }

    public void end() {
        if (current == null || combatants == null || combatants.isEmpty()) {
            return;
        }
        if (current.equals(combatants.getLast())) {
            this.go(combatants.getFirst(), round + 1);
        } else {
            this.go(combatants.get(combatants.indexOf(current) + 1), round);
        }
    }

    public void putCharBefore(String charName, String before) {
        int index = findIndexByNameOrAbbr(before);
        Combatant ch = findCharByNameOrAbbre(charName);
        if (ch == null || index == -1) {
            return;
        }

        if (findCharByNameOrAbbre(charName).equals(current)) {
            end();
        }

        if (index == 0) {
            ch.setInit(combatants.get(index).getInit() + 1);
        } else {
            ch.setInit((combatants.get(index).getInit() + combatants.get(
                    index - 1).getInit()) / 2);
        }
        restoreOrder();
    }

    public void putCharAfter(String charName, String after) {
        int index = findIndexByNameOrAbbr(after);
        Combatant ch = findCharByNameOrAbbre(charName);
        if (ch == null || index == -1) {
            return;
        }

        if (findCharByNameOrAbbre(charName).equals(current)) {
            end();
        }

        if (combatants.getLast().equals(combatants.get(index))) {
            ch.setInit(combatants.get(index).getInit() - 1);
        } else {
            ch.setInit((combatants.get(index).getInit() + combatants.get(
                    index + 1).getInit()) / 2);
        }
        restoreOrder();
    }

    public void removeChar(String charName) {
        if (findCharByNameOrAbbre(charName).equals(current)) {
            end();
        }
        combatants.remove(findCharByNameOrAbbre(charName));
    }

    public void renameChar(String charName, String newName) {
        Combatant ch = findCharByNameOrAbbre(charName);
        if (ch != null) {
            ch.rename(newName);
        }
    }

    public void heal(String charName, int healValue) {
        Combatant ch = findCharByNameOrAbbre(charName);
        if (ch != null) {
            ch.heal(healValue);
        }
    }

    public void damage(String charName, int dmg) {
        Combatant ch = findCharByNameOrAbbre(charName);
        if (ch != null) {
            ch.damage(dmg);
        }
    }

    public void addThp(String charName, int thp) {
        Combatant ch = findCharByNameOrAbbre(charName);
        if (ch != null) {
            ch.setThp(thp);
        }
    }

    public void applyState(String charName, String stateStr) {
        Combatant ch = findCharByNameOrAbbre(charName);
        if (ch != null) {
            State state = State.parseState(stateStr, round, current.getInit());
            ch.applyState(state);
        }
    }

    public void removeState(String charName, String stateStr) {
        Combatant ch = findCharByNameOrAbbre(charName);
        if (ch != null) {
            State state = State.parseState(stateStr, -1, -1);
            ch.removeState(ch.matchState(state));
        }
    }

    public Combatant getCurrent() {
        return current;
    }

    public void clear() {
        this.combatants = new LinkedList<Combatant>();
        this.current = null;
        this.round = -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (round == 0) {
            sb.append("surprise: ");
        } else if (round > 0) {
            sb.append("r").append(round).append(": ");
        } else {
            sb.append("setup: ");
        }
        for (Combatant ch : combatants) {
            if (!ch.equals(combatants.getFirst())) {
                sb.append(", ");
            }
            if (ch.equals(current)) {
                sb.append(ch.toOnTurnString());
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public void restoreOrder() {
        Collections.sort(combatants, new Comparator<Combatant>() {
            @Override
            public int compare(Combatant o1, Combatant o2) {
                return (int) Math.signum(o2.getInit() - o1.getInit());
            }
        });
    }

    public int findIndexByNameOrAbbr(String charName) {
        int i = findIndexByName(charName);
        if (i != -1) {
            return i;
        } else {
            return findIndexByAbbr(charName);
        }
    }

    public int findIndexByAbbr(String charName) {
        int i = 0;
        for (Combatant c : combatants) {
            if (AbbreviationUtil.isPrefixAbbre(charName, c.getName())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int findIndexByName(String charName) {
        int i = 0;
        for (Combatant c : combatants) {
            if (c.getName().equalsIgnoreCase(charName)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public Combatant findCharByNameOrAbbre(String charName) {
        Combatant c = findCharByName(charName);
        if (c != null) {
            return c;
        } else {
            return findCharByAbbr(charName);
        }
    }

    public Combatant findCharByName(String charName) {
        for (Combatant c : combatants) {
            if (c.getName().equalsIgnoreCase(charName)) {
                return c;
            }
        }
        return null;
    }

    public Combatant findCharByAbbr(String charName) {
        for (Combatant c : combatants) {
            if (AbbreviationUtil.isPrefixAbbre(charName, c.getName())) {
                return c;
            }
        }
        return null;
    }

    private void fireInitiativeChange(int newRound, double newInit) {
        int oldRound = round;
        double oldInit = current.getInit();
        for (Combatant ch : combatants) {
            List<String> stateMsgsFromChar = ch
                    .checkStateBehavior(new InitiativePassesEvent(oldRound,
                            oldInit, newRound, newInit));
            if (stateMsgsFromChar != null && !stateMsgsFromChar.isEmpty()) {
                eventResultBuffer.addAll(stateMsgsFromChar);
            }
        }
    }

    public List<String> getEventResultBuffer() {
        List<String> events = new ArrayList<String>(eventResultBuffer);
        eventResultBuffer.clear();
        return events;
    }

    @Override
    public Battle clone() {
        Battle clone = null;
        try {
            clone = (Battle) super.clone();
            if (current != null) {
                clone.current = this.current.clone();
            }
            clone.combatants = new LinkedList<Combatant>();
            for (Combatant ch : combatants) {
                if (ch != null) {
                    clone.combatants.add(ch.clone());
                } else {
                    clone.combatants.add(null);
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((combatants == null) ? 0 : combatants.hashCode());
        result = prime * result + ((current == null) ? 0 : current.hashCode());
        result = prime * result + round;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Battle other = (Battle) obj;
        if (combatants == null) {
            if (other.combatants != null)
                return false;
        } else if (!combatants.equals(other.combatants))
            return false;
        if (current == null) {
            if (other.current != null)
                return false;
        } else if (!current.equals(other.current))
            return false;
        if (round != other.round)
            return false;
        return true;
    }

    public LinkedList<Combatant> getChars() {
        return combatants;
    }

    public int getRound() {
        return round;
    }
}
