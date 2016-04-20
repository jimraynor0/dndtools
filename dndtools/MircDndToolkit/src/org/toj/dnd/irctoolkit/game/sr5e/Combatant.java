package org.toj.dnd.irctoolkit.game.sr5e;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Combatant {
    private String name;
    private int init;
    private ConditionMonitor stun = new ConditionMonitor("s");
    private ConditionMonitor physical = new ConditionMonitor("p");

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    // (10)name-3p-5s
    public String getStatusForTopic() {
        StringBuilder status = new StringBuilder("(").append(init).append("").append(name)
                .append(physical.toStatusString()).append(stun.toStatusString());
        return status.toString();
    }
}
