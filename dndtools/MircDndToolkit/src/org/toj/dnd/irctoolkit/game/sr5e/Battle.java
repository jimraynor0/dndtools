package org.toj.dnd.irctoolkit.game.sr5e;

import java.util.LinkedList;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Battle {
    private int round = 0;
    private int pass = 1;
    private LinkedList<Combatant> combatants = new LinkedList<>();
    private Combatant current;

    public void addCombatantByInit(PC pc, int init) {
        pc.setInit(init);
        combatants.add(pc);
        sort();
    }

    public void addCombatantByInit(String ch, int init) {
        Combatant combatant = new Combatant();
        combatant.setName(ch);
        combatant.setInit(init);
        combatants.add(combatant);
        sort();
    }

    public void setCombatantInit(String name, int init) {
        Combatant ch = findCharByNameOrAbbre(name);
        if (ch == null) {
            return;
        }

        ch.setInit(init);
        sort();
    }

    public Combatant findCharByNameOrAbbre(String name) {
        return combatants.stream().filter(n -> n.getName().startsWith(name)).findFirst().orElse(null);
    }

    public void sort() {
        combatants.sort((c1, c2) -> c2.getInit() - c1.getInit());
    }

    public void endTurn() {
        if (current == null) {
            return;
        }
        if (current == getActiveCombatants().getLast()) {
            endPass();
        } else {
            current = getActiveCombatants().get(getActiveCombatants().indexOf(current) + 1);
        }
    }

    private LinkedList<Combatant> getActiveCombatants() {
        return new LinkedList<>(combatants.stream().filter(c -> c.getInit() > 0).collect(Collectors.toList()));
    }

    private void endPass() {
        getActiveCombatants().stream().forEach(c -> c.reduceInit(10));
        if (getActiveCombatants().isEmpty()) {
            endRound();
        } else {
            pass++;
            sort();
            current = getActiveCombatants().getFirst();
        }
    }

    private void endRound() {
        round++;
        pass = 1;
        current = null;
    }

    public void startRound(int startAt) {
        this.pass = 1;
        this.round = startAt;
        current = combatants.getFirst();
    }

    public void remove(String name) {
        Combatant removed = findCharByNameOrAbbre(name);
        if (removed == current) {
            endTurn();
        }
        combatants.remove(removed);
    }

    public Combatant getCurrent() {
        return current;
    }

    public String generateTopic() {
        StringBuilder sb = new StringBuilder();
        if (round <= 0) {
            sb.append("setup: ");
        } else {
            sb.append("r").append(round).append("p").append(pass).append(": ");
        }

        sb.append(combatants.stream().map(c -> c.toTopicString(c == current)).collect(Collectors.joining(", ")));
        return sb.toString();
    }

    public int getRound() {
        return round;
    }
}
