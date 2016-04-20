package org.toj.dnd.irctoolkit.game.sr5e;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Combatant {
    private int init;
    private ConditionMonitor stun = new ConditionMonitor();
    private ConditionMonitor physical = new ConditionMonitor();

    public int getInit() {
        return init;
    }

    public void setInit(int init) {
        this.init = init;
    }

    public ConditionMonitor getStun() {
        return stun;
    }

    public ConditionMonitor getPhysical() {
        return physical;
    }
}
