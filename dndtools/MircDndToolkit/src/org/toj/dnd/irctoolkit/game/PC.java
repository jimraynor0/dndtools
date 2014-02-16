package org.toj.dnd.irctoolkit.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class PC extends Combatant {

    private int xp;
    private int hp;
    private int maxHp;
    private int surge;
    private int maxSurge;
    private int ap;
    private int pp;
    private int maxPp;
    private int initMod;
    private Map<String, Power> encounterPowers;
    private Map<String, Power> dailyPowers;
    private boolean isPsionic = false;

    public PC(String name) {
        super(name);
        this.encounterPowers = new HashMap<String, Power>();
        this.dailyPowers = new HashMap<String, Power>();
    }

    @SuppressWarnings("unchecked")
    public PC(Element e) {
        super(e);
        this.xp = Integer.parseInt(e.elementTextTrim("xp"));
        this.hp = Integer.parseInt(e.elementTextTrim("hp"));
        this.maxHp = Integer.parseInt(e.elementTextTrim("maxHp"));
        this.maxSurge = Integer.parseInt(e.elementTextTrim("maxSurge"));
        this.surge = Integer.parseInt(e.elementTextTrim("surge"));
        this.ap = Integer.parseInt(e.elementTextTrim("ap"));
        if (e.element("maxPp") != null) {
            this.isPsionic = true;
            this.pp = Integer.parseInt(e.elementTextTrim("pp"));
            this.maxPp = Integer.parseInt(e.elementTextTrim("maxPp"));
        }
        String initModSave = e.elementTextTrim("initMod");
        if (initModSave != null) {
            this.initMod = Integer.parseInt(initModSave);
        }

        this.encounterPowers = new HashMap<String, Power>();
        this.dailyPowers = new HashMap<String, Power>();
        if (e.element("encounterPowers") != null) {
            Iterator<Element> i = e.element("encounterPowers")
                    .elementIterator();
            while (i.hasNext()) {
                Power power = new Power(i.next());
                this.encounterPowers.put(power.getName(), power);
            }
        }

        if (e.element("dailyPowers") != null) {
            Iterator<Element> i = e.element("dailyPowers").elementIterator();
            while (i.hasNext()) {
                Power power = new Power(i.next());
                this.dailyPowers.put(power.getName(), power);
            }
        }
    }

    public void damage(int dmg) {
        boolean bloodied = isBloodied();
        if (getThp() >= dmg) {
            setThp(getThp() - dmg);
            return;
        }
        if (getThp() > 0) {
            dmg -= getThp();
            setThp(0);
        }
        hp -= dmg;
        if (!bloodied && isBloodied()) {
            super.applyState(State.parseState("bloodied|eoe"));
        }
    }

    public void heal(int heal) {
        boolean bloodied = isBloodied();
        // commented out for 3r
        if (hp < 0) {
            hp = 0;
        }
        hp += heal;
        if (hp > maxHp) {
            hp = maxHp;
        }
        if (bloodied && !isBloodied()) {
            super.removeState(State.parseState("bloodied|eoe"));
        }
    }

    public boolean isBloodied() {
        return hp <= maxHp / 2;
    }

    public void recordAp(int usage) {
        this.ap -= usage;
        if (this.ap < 0) {
            this.ap = 0;
        }
    }

    public void recordSurge(int usage) {
        this.surge -= usage;
        if (surge < 0) {
            surge = 0;
        }
    }

    public void recordPp(int usage) {
        this.pp -= usage;
        if (pp < 0) {
            pp = 0;
        }
    }

    public void addPower(String type, Power power) {
        if (type.startsWith("e")) {
            this.encounterPowers.put(power.getName(), power);
        } else {
            this.dailyPowers.put(power.getName(), power);
        }
    }

    public void removePower(String type, String powerName) {
        if (type.startsWith("e")) {
            this.encounterPowers.remove(powerName);
        } else {
            this.dailyPowers.remove(powerName);
        }
    }

    public void removePower(String powerName) {
        Power power = findPower(powerName);
        if (power != null) {
            this.encounterPowers.remove(powerName);
            this.dailyPowers.remove(powerName);
        }
    }

    public void applyShortRest() {
        for (Power power : encounterPowers.values()) {
            power.setCharges(power.getMaxCharges());
        }
        this.pp = maxPp;
        this.setThp(0);
    }

    public void applyExtendedRest() {
        applyShortRest();
        for (Power power : dailyPowers.values()) {
            power.setCharges(power.getMaxCharges());
        }
        this.ap = 1;
        this.hp = maxHp;
        this.surge = maxSurge;
    }

    public void applyMilestone() {
        ap++;
    }

    public String toStatString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PC: ").append(this.getName()).append("\r\n");
        sb.append("XP: ").append(xp).append("\r\n");
        sb.append("HP: ").append(hp).append("/").append(maxHp);
        if (getThp() > 0) {
            sb.append("(Temporary HP: ").append(this.getThp()).append(")");
        }
        sb.append("\r\n");
        sb.append("Surge: ").append(surge).append("/").append(maxSurge)
                .append("\r\n");
        sb.append("AP: ").append(this.ap).append("\r\n");
        if (isPsionic) {
            sb.append("Psionic Point: ").append(pp).append("/").append(maxPp)
                    .append("\r\n");
        }
        sb.append("Init Modifier: ").append(this.initMod).append("\r\n");
        if (!encounterPowers.isEmpty()) {
            sb.append("Encounter Powers").append(" - ");
            boolean first = true;
            for (Power power : encounterPowers.values()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                if (power.getCharges() == 0) {
                    sb.append((char) 3).append(14);
                }
                if (power.getGroup() != null) {
                    sb.append(power.getGroup()).append(":");
                }
                sb.append(power.getName()).append("(")
                        .append(power.getCharges()).append("/")
                        .append(power.getMaxCharges()).append(")");
                if (power.getCharges() == 0) {
                    sb.append((char) 15);
                }
            }
            sb.append("\r\n");
        }
        if (!dailyPowers.isEmpty()) {
            sb.append("Daily Powers").append(" - ");
            boolean first = true;
            for (Power power : dailyPowers.values()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                if (power.getCharges() <= 0) {
                    sb.append((char) 3).append(14);
                }
                if (power.getGroup() != null) {
                    sb.append(power.getGroup()).append(":");
                }
                sb.append(power.getName()).append("(")
                        .append(power.getCharges()).append("/")
                        .append(power.getMaxCharges()).append(")");
                if (power.getCharges() <= 0) {
                    sb.append((char) 15);
                }
            }
            sb.append("\r\n");
        }
        if (states != null && !states.isEmpty()) {
            sb.append("Existing Effects: ");
            for (State s : states) {
                sb.append(s.getName()).append("|").append(s.getEndCondition());
                sb.append(s != states.getLast() ? ", " : "\r\n");
            }
        }
        return sb.toString();
    }

    public Element toXmlElement() {
        Element e = super.toXmlElement();
        e.setName("pc");
        e.add(XmlUtil.textElement("xp", String.valueOf(xp)));
        e.add(XmlUtil.textElement("hp", String.valueOf(hp)));
        e.add(XmlUtil.textElement("maxHp", String.valueOf(maxHp)));
        e.add(XmlUtil.textElement("surge", String.valueOf(surge)));
        e.add(XmlUtil.textElement("maxSurge", String.valueOf(maxSurge)));
        e.add(XmlUtil.textElement("ap", String.valueOf(ap)));
        if (isPsionic) {
            e.add(XmlUtil.textElement("pp", String.valueOf(pp)));
            e.add(XmlUtil.textElement("maxPp", String.valueOf(maxPp)));
        }
        e.add(XmlUtil.textElement("initMod", String.valueOf(initMod)));

        if (!encounterPowers.isEmpty()) {
            Element eps = e.addElement("encounterPowers");
            for (Power power : encounterPowers.values()) {
                eps.add(power.toXmlElement());
            }
        }

        if (!dailyPowers.isEmpty()) {
            Element dps = e.addElement("dailyPowers");
            for (Power power : dailyPowers.values()) {
                dps.add(power.toXmlElement());
            }
        }
        return e;
    }

    public void setHp(int hp) {
        boolean bloodied = isBloodied();
        this.hp = hp;
        if (!bloodied && isBloodied()) {
            super.applyState(State.parseState("bloodied|eoe"));
        }
        if (bloodied && !isBloodied()) {
            super.removeState(State.parseState("bloodied|eoe"));
        }
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setSurge(int surge) {
        this.surge = surge;
    }

    public void setMaxSurge(int maxSurge) {
        this.maxSurge = maxSurge;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

    public void setPp(int pp) {
        this.isPsionic = true;
        this.pp = pp;
    }

    public void setMaxPp(int maxPp) {
        this.isPsionic = true;
        this.maxPp = maxPp;
    }

    public void usePower(String name) throws PowerDepleteException {
        Power power = findPower(name);
        if (power != null) {
            if (power.getCharges() > 0) {
                power.setCharges(power.getCharges() - 1);
            } else {
                throw new PowerDepleteException();
            }
        }
        if (power.getGroup() != null) {
            for (Power sameGroupPower : this.getPowersInGroup(power.getGroup())) {
                if (sameGroupPower.getCharges() > 0) {
                    sameGroupPower.setCharges(sameGroupPower.getCharges() - 1);
                }
            }
        }
    }

    public List<Power> getPowersInGroup(String group) {
        List<Power> powers = new ArrayList<Power>();
        for (Power power : encounterPowers.values()) {
            if (group.equals(power.getGroup())) {
                powers.add(power);
            }
        }
        for (Power power : dailyPowers.values()) {
            if (group.equals(power.getGroup())) {
                powers.add(power);
            }
        }
        return powers;
    }

    public void regainPower(String name) {
        Power power = findPower(name);
        if (power != null) {
            power.setCharges(power.getCharges() + 1);
        }
    }

    public String readPower(String name) {
        Power power = findPower(name);
        if (power == null) {
            return null;
        } else {
            return power.getName() + " - " + power.getDescription();
        }
    }

    public Power findPower(String name) {
        for (String key : encounterPowers.keySet()) {
            if (key.equalsIgnoreCase(name)
                    || AbbreviationUtil.isAbbre(name, key)) {
                return encounterPowers.get(key);
            }
        }
        for (String key : dailyPowers.keySet()) {
            if (key.equalsIgnoreCase(name)
                    || AbbreviationUtil.isAbbre(name, key)) {
                return dailyPowers.get(key);
            }
        }
        return null;
    }

    protected String getHpExpression() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(hp).append("/").append(maxHp);
        if (getThp() > 0) {
            sb.append("+").append(getThp());
        }
        sb.append(")");
        if (isBloodied()) {
            return IrcColoringUtil.paint(sb.toString(), Color.RED.getCode());
        }
        return sb.toString();
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getInitMod() {
        return initMod;
    }

    public void setInitMod(int initMod) {
        this.initMod = initMod;
    }

    public boolean isPsionic() {
        return isPsionic;
    }

    public void setPsionic(boolean isPsionic) {
        this.isPsionic = isPsionic;
    }
}
