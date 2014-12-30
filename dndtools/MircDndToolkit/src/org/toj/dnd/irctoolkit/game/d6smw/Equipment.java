package org.toj.dnd.irctoolkit.game.d6smw;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Equipment {
    private String name;
    private String model;
    private int cd;
    private TimePoint readyOn;
    private int heat;
    private boolean active;

    public Equipment(Element e) {
        name = e.elementTextTrim("name");
        model = e.elementTextTrim("model");
        if (e.element("cd") != null) {
            cd = Integer.parseInt(e.elementTextTrim("cd"));
        } else {
            cd = 0;
        }
        if (e.element("readyOn") != null) {
            readyOn =
                new TimePoint(Integer.parseInt(e
                    .elementTextTrim("readyOnRound")), Integer.parseInt(e
                    .elementTextTrim("readyOnInit")));
        }
        if (e.element("heat") != null) {
            heat = Integer.parseInt(e.elementTextTrim("heat"));
        } else {
            heat = 0;
        }
        if (e.element("active") != null) {
            active = Boolean.parseBoolean(e.elementTextTrim("active"));
        } else {
            active = true;
        }
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement(getType());
        e.add(XmlUtil.textElement("name", name));
        e.add(XmlUtil.textElement("model", model));
        e.add(XmlUtil.textElement("cd", String.valueOf(cd)));
        if (readyOn != null) {
            e.add(XmlUtil.textElement("readyOnRound", String.valueOf(readyOn.round)));
            e.add(XmlUtil.textElement("readyOnInit", String.valueOf(readyOn.init)));
        }
        e.add(XmlUtil.textElement("heat", String.valueOf(heat)));
        e.add(XmlUtil.textElement("active", String.valueOf(active)));

        return e;
    }

    public String activate(TimePoint activatingOn) {
        if (!active) {
            return name + "已经损坏.";
        }
        if (readyOn != null && readyOn.after(activatingOn)) {
            return name + "还在CD中.";
        }
        this.readyOn =
            new TimePoint(activatingOn.round + cd, activatingOn.init);
        return null;
    }

    public String toFullStatString(TimePoint current) {
        String nameModelStr = name + "(" + model + ")";
        if (!isActive()) {
            nameModelStr = IrcColoringUtil.paint(nameModelStr, Color.RED.getCode());
        }
        if (current == null || readyOn == null || !readyOn.after(current)) {
            return nameModelStr;
        } else {
            return nameModelStr + " 剩余" + current.getRoundsUntil(readyOn) + "回合CD";
        }
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
