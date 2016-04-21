package org.toj.dnd.irctoolkit.game.sr5e;

import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;

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

    // exposed only to battle/game
    void setInit(int init) {
        this.init = init;
    }

    public ConditionMonitor getStun() {
        return stun;
    }

    public ConditionMonitor getPhysical() {
        return physical;
    }

    public void reduceInit(int amount) {
        init -= amount;
        if (init < 0) {
            init = 0;
        }
    }

    public void damage(int value, String type) {
        if ("p".equalsIgnoreCase(type)) {
            physical.updateWound(0 - value);
        } else {
            int overflow = stun.updateWound(0 - value);
            if (overflow > 0) {
                physical.updateWound(0 - overflow / 2);
            }
        }
    }

    public void heal(int value, String type) {
        if ("p".equalsIgnoreCase(type)) {
            physical.updateWound(value);
        } else {
            stun.updateWound(value);
        }
    }

    /**
     * @return sth like (10)name-3p-5s
     */
    public String toTopicString(boolean isCurrent) {
        StringBuilder status = new StringBuilder("(").append(init).append(")")
                .append(isCurrent ? IrcColoringUtil.paint(name, Color.RED.getCode()) : name)
                .append(physical.toStatusString()).append(stun.toStatusString());
        return status.toString();
    }
}
