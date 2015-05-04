package org.toj.dnd.irctoolkit.game.d6smw;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
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
