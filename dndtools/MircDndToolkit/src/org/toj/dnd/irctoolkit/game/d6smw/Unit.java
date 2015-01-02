package org.toj.dnd.irctoolkit.game.d6smw;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Unit {
    private int init;
    private String name;
    private String model;
    private int speed;
    private String direction;
    private int hp;

    public Unit() {

    }

    public Unit(String name) {
        this.name = name;
    }

    public Unit(String name, int init) {
        this(name);
        this.init = init;
    }

    public Unit(Element e) {
        name = e.elementTextTrim("name");
        model = e.elementTextTrim("model");
        if (e.element("init") != null) {
            init = Integer.parseInt(e.elementTextTrim("init"));
        } else {
            init = 0;
        }
        if (e.element("speed") != null) {
            speed = Integer.parseInt(e.elementTextTrim("speed"));
        } else {
            speed = 0;
        }

        if (e.element("direction") != null) {
            direction = e.elementTextTrim("direction");
        }
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement("mech");
        e.add(XmlUtil.textElement("name", name));
        e.add(XmlUtil.textElement("model", model));
        e.add(XmlUtil.textElement("init", init));
        if (!StringUtils.isEmpty(direction)) {
            e.add(XmlUtil.textElement("direction", direction));
        }
        e.add(XmlUtil.textElement("speed", String.valueOf(speed)));
        return e;
    }

    public String toTopicString(boolean onTurn) {
        StringBuilder sb = new StringBuilder();
        if (onTurn) {
            sb.append(IrcColoringUtil.paint(name, Color.RED.getCode()));
        } else {
            sb.append(name);
        }
        sb.append(getHpExpression());
        return sb.toString();
    }

    private String getHpExpression() {
        if (hp < 0) {
            return String.valueOf(hp);
        }
        return "";
    }

    public void damage(int value, String section) {
        hp -= value;
    }

    public void quickRepair(int value, String section) {
        hp += value;
        if (hp > 0) {
            hp = 0;
        }
    }

    public boolean hasHeat() {
        return false;
    }

    public void sinkHeat(int calcInitPassesTimes) {
        throw new UnsupportedOperationException();
    }

    public int getInit() {
        return init;
    }

    public void setInit(int init) {
        this.init = init;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
