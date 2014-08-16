package org.toj.dnd.irctoolkit.game.encounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.Power;
import org.toj.dnd.irctoolkit.game.PowerDepleteException;
import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class NPC extends Combatant {

    private boolean isAlly;
    private int hp;
    private int maxHp;
    private int surge;
    private int maxSurge;
    private int ap;
    private int initMod;
    private Map<String, RechargablePower> rechargablePowers;
    private Map<String, Power> encounterPowers;

    public NPC(String name) {
        super(name);
        this.encounterPowers = new HashMap<String, Power>();
        this.rechargablePowers = new HashMap<String, RechargablePower>();
    }

    @SuppressWarnings("unchecked")
    public NPC(Element e) {
        super(e);
        this.isAlly = Boolean.parseBoolean(e.elementTextTrim("isAlly"));
        this.hp = Integer.parseInt(e.elementTextTrim("hp"));
        this.maxHp = Integer.parseInt(e.elementTextTrim("maxHp"));
        this.maxSurge = Integer.parseInt(e.elementTextTrim("maxSurge"));
        this.surge = Integer.parseInt(e.elementTextTrim("surge"));
        this.ap = Integer.parseInt(e.elementTextTrim("ap"));
        String initModSave = e.elementTextTrim("initMod");
        if (initModSave != null) {
            this.initMod = Integer.parseInt(initModSave);
        }

        this.encounterPowers = new HashMap<String, Power>();
        this.rechargablePowers = new HashMap<String, RechargablePower>();
        if (e.element("encounterPowers") != null) {
            Iterator<Element> i = e.element("encounterPowers")
                    .elementIterator();
            while (i.hasNext()) {
                Power power = new Power(i.next());
                this.encounterPowers.put(power.getName(), power);
            }
        }

        if (e.element("rechargablePowers") != null) {
            Iterator<Element> i = e.element("rechargablePowers")
                    .elementIterator();
            while (i.hasNext()) {
                RechargablePower power = new RechargablePower(i.next());
                this.rechargablePowers.put(power.getName(), power);
            }
        }
    }

    public NPC(Element combatant, Encounter encounter) {
        this(combatant);
    }

    public NPC(String name, NpcTemplate npcTemplate) {
        this(name);
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

    public void addPower(String type, Power power) {
        if (type.startsWith("e")) {
            this.encounterPowers.put(power.getName(), power);
        } else if (power instanceof RechargablePower) {
            this.rechargablePowers.put(power.getName(),
                    (RechargablePower) power);
        }
    }

    public void removePower(String type, String powerName) {
        if (type.startsWith("e")) {
            this.encounterPowers.remove(powerName);
        } else {
            this.rechargablePowers.remove(powerName);
        }
    }

    public void removePower(String powerName) {
        Power power = findPower(powerName);
        if (power != null) {
            this.encounterPowers.remove(powerName);
            this.rechargablePowers.remove(powerName);
        }
    }

    public void applyMilestone() {
        ap++;
    }

    public String toStatString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NPC: ").append(this.getName()).append("\r\n");
        sb.append("Is Ally: ").append(isAlly).append("\r\n");
        sb.append("HP: ").append(hp).append("/").append(maxHp);
        if (getThp() > 0) {
            sb.append("(Temporary HP: ").append(this.getThp()).append(")");
        }
        sb.append("\r\n");
        sb.append("Surge: ").append(surge).append("/").append(maxSurge)
                .append("\r\n");
        sb.append("AP: ").append(this.ap).append("\r\n");
        sb.append("Init Modifier: ").append(this.initMod).append("\r\n");
        if (!rechargablePowers.isEmpty()) {
            sb.append("Rechargable Powers").append(": ");
            boolean first = true;
            for (Power power : rechargablePowers.values()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                if (power.getCharges() <= 0) {
                    sb.append((char) 3).append(14);
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
        if (!encounterPowers.isEmpty()) {
            sb.append("Encounter Powers").append(": ");
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
                sb.append(power.getName()).append("(")
                        .append(power.getCharges()).append("/")
                        .append(power.getMaxCharges()).append(")");
                if (power.getCharges() == 0) {
                    sb.append((char) 15);
                }
            }
            sb.append("\r\n");
        }
        if (states != null && !states.isEmpty()) {
            sb.append("Existing Effects: ");
            for (State s : states) {
                sb.append(s.toString());
                sb.append(s != states.getLast() ? ", " : "\r\n");
            }
        }
        return sb.toString();
    }

    public Element toXmlElement() {
        Element e = super.toXmlElement();
        e.setName("npc");
        e.add(XmlUtil.textElement("isAlly", String.valueOf(isAlly)));
        e.add(XmlUtil.textElement("hp", String.valueOf(hp)));
        e.add(XmlUtil.textElement("maxHp", String.valueOf(maxHp)));
        e.add(XmlUtil.textElement("surge", String.valueOf(surge)));
        e.add(XmlUtil.textElement("maxSurge", String.valueOf(maxSurge)));
        e.add(XmlUtil.textElement("ap", String.valueOf(ap)));
        e.add(XmlUtil.textElement("initMod", String.valueOf(initMod)));

        if (!encounterPowers.isEmpty()) {
            Element eps = e.addElement("encounterPowers");
            for (Power power : encounterPowers.values()) {
                eps.add(power.toXmlElement());
            }
        }

        if (!rechargablePowers.isEmpty()) {
            Element dps = e.addElement("rechargablePowers");
            for (Power power : rechargablePowers.values()) {
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

    private List<Power> getPowersInGroup(String group) {
        List<Power> powers = new ArrayList<Power>();
        for (Power power : encounterPowers.values()) {
            if (power.getGroup().equals(group)) {
                powers.add(power);
            }
        }
        for (Power power : rechargablePowers.values()) {
            if (power.getGroup().equals(group)) {
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
        for (String key : rechargablePowers.keySet()) {
            if (key.equalsIgnoreCase(name)
                    || AbbreviationUtil.isAbbre(name, key)) {
                return rechargablePowers.get(key);
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

    public int getInitMod() {
        return initMod;
    }

    public void setInitMod(int initMod) {
        this.initMod = initMod;
    }

    public boolean isAlly() {
        return isAlly;
    }

    public void setAlly(boolean isAlly) {
        this.isAlly = isAlly;
    }
}
