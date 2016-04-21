package org.toj.dnd.irctoolkit.game.sr5e;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Battle {
    private LinkedList<Combatant> combatants = new LinkedList<>();

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

    private void sort() {
        combatants.sort((c1, c2) -> c2.getInit() - c1.getInit());
    }
}
