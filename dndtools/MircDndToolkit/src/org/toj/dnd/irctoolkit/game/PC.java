package org.toj.dnd.irctoolkit.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class PC extends Combatant {

    private int xp;
    private int hp;
    private int maxHp;
    private int pp;
    private int maxPp;
    private int initMod;
    private Map<String, Power> encounterPowers;
    private Map<String, Power> dailyPowers;
    private boolean isPsionic = false;
    private Map<String, Item> items;

    public PC(String name) {
        super(name);
        this.encounterPowers = new HashMap<String, Power>();
        this.dailyPowers = new HashMap<String, Power>();
        this.items = new HashMap<String, Item>();
    }

    @SuppressWarnings("unchecked")
    public PC(Element e) {
        super(e);
        this.xp = Integer.parseInt(e.elementTextTrim("xp"));
        this.hp = Integer.parseInt(e.elementTextTrim("hp"));
        this.maxHp = Integer.parseInt(e.elementTextTrim("maxHp"));
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

        this.items = new HashMap<String, Item>();
        if (e.element("items") != null) {
            Iterator<Element> i = e.element("items").elementIterator();
            while (i.hasNext()) {
                Item c = new Item(i.next());
                this.items.put(c.getName(), c);
            }
        }
    }

    public void damage(int dmg) {
        if (getThp() >= dmg) {
            setThp(getThp() - dmg);
            return;
        }
        if (getThp() > 0) {
            dmg -= getThp();
            setThp(0);
        }
        hp -= dmg;
    }


    public void heal(int heal) {
        hp += heal;
        if (hp > maxHp) {
            hp = maxHp;
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
    }

    public void applyExtendedRest() {
        applyShortRest();
        for (Power power : dailyPowers.values()) {
            power.setCharges(power.getMaxCharges());
        }
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
                sb.append(s.toString());
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

        if (!items.isEmpty()) {
            Element dps = e.addElement("items");
            for (Item item : items.values()) {
                dps.add(item.toXmlElement());
            }
        }
        return e;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
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

    public String removeItem(Item item) {
        if (!items.containsKey(item.getName())) {
            return this.getName() + "������Ʒ��û��[" + item.getName() + "]";
        }
        Item loot = items.get(item.getName());
        if (loot.getCharges() < item.getCharges()) {
            return this.getName() + "���е���Ʒ[" + item.getName() + "]����ֻ��" + loot.getCharges();
        }
        loot.decreaseCharge(item.getCharges());
        if (loot.getCharges() == 0) {
            items.remove(loot.getName());
        }
        return null;
    }

    public void addItem(Item item) {
        if (!items.containsKey(item.getName())) {
            items.put(item.getName(), item);
        } else {
            items.get(item.getName()).increaseCharge(item.getCharges());
        }
    }

    protected String getHpExpression() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(hp).append("/").append(maxHp);
        if (getThp() > 0) {
            sb.append("+").append(getThp());
        }
        sb.append(")");
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

    public Map<String, Item> getItems() {
        return items;
    }

    public void setItems(Map<String, Item> consumables) {
        this.items = consumables;
    }
}
