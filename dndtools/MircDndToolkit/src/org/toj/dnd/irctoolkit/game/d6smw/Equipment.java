package org.toj.dnd.irctoolkit.game.d6smw;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Equipment {
    private String name;
    private String model;
    private int cd;
    private TimePoint readyOn;
    private int heat;
    private boolean active;

    public String activate(TimePoint activatingOn) {
        if (!active) {
            return name + "已经损坏.";
        }
        if (readyOn != null && readyOn.after(activatingOn)) {
            return name + "还在CD中.";
        }
        this.readyOn = new TimePoint(activatingOn.round + cd, activatingOn.init);
        return null;
    }

    public String toStatString(TimePoint current) {
        if (!isActive()) {
            return IrcColoringUtil.paint(name, Color.RED.getCode());
        }
        if (!isOnCd(current)) {
            return name;
        } else {
            return IrcColoringUtil.paint(
                    name + "(CD " + current.getRoundsUntil(readyOn) + ")",
                    Color.ORANGE.getCode());
        }
    }

    public String toFullStatString(TimePoint current) {
        String nameModelStr = name + "(" + model + ")";
        if (!isActive()) {
            nameModelStr = IrcColoringUtil.paint(nameModelStr,
                    Color.RED.getCode());
        }
        if (!isOnCd(current)) {
            return nameModelStr;
        } else {
            return nameModelStr
                    + IrcColoringUtil.paint(
                            " 剩余" + current.getRoundsUntil(readyOn) + "回合CD",
                            Color.NAVY.getCode());
        }
    }

    public boolean isOnCd(TimePoint current) {
        return current != null && readyOn != null && readyOn.after(current);
    }

    public boolean isActive() {
        return active;
    }

    public void active() {
        active = true;
    }

    public void deactive() {
        active = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public TimePoint getReadyOn() {
        return readyOn;
    }

    public void setReadyOn(TimePoint readyOn) {
        this.readyOn = readyOn;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    protected String getType() {
        return "equipment";
    }
}
