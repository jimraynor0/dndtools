package org.toj.dnd.irctoolkit.game.d6smw;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Equipment {
    private String name;
    private int cd;
    private TimePoint readyOn;
    private int heat;
    private boolean active;

    public Equipment(Element e) {
        name = e.elementTextTrim("name");
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
        Element e = DocumentHelper.createElement("section");
        e.add(XmlUtil.textElement("name", name));
        e.add(XmlUtil.textElement("cd", String.valueOf(cd)));
        e.add(XmlUtil.textElement("readyOnRound", String.valueOf(readyOn.round)));
        e.add(XmlUtil.textElement("readyOnInit", String.valueOf(readyOn.init)));
        e.add(XmlUtil.textElement("heat", String.valueOf(heat)));
        e.add(XmlUtil.textElement("active", String.valueOf(active)));

        return e;
    }

    public String activate(TimePoint activatingOn) {
        if (!active) {
            return name + "已经损坏.";
        }
        if (readyOn.before(activatingOn)) {
            return name + "还在CD中.";
        }
        this.readyOn =
            new TimePoint(activatingOn.round + cd, activatingOn.init);
        return null;
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

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }
}
