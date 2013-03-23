package org.toj.dnd.irctoolkit.game.encounter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.Power;
import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;

public class NpcTemplate extends Combatant {

    private boolean isAlly;
    private int maxHp;
    private int maxSurge;
    private int ap;
    private int initMod;
    private Map<String, RechargablePower> rechargablePowers;
    private Map<String, Power> encounterPowers;

    public NpcTemplate(String name) {
        super(name);
        this.encounterPowers = new HashMap<String, Power>();
        this.rechargablePowers = new HashMap<String, RechargablePower>();
    }

    @SuppressWarnings("unchecked")
    public NpcTemplate(Element e) {
        super(e);
        this.isAlly = Boolean.parseBoolean(e.elementTextTrim("isAlly"));
        this.maxHp = Integer.parseInt(e.elementTextTrim("maxHp"));
        this.maxSurge = Integer.parseInt(e.elementTextTrim("maxSurge"));
        this.ap = Integer.parseInt(e.elementTextTrim("ap"));
        String initModSave = e.elementTextTrim("initMod");
        if (initModSave != null) {
            this.initMod = Integer.parseInt(initModSave);
        }

        this.encounterPowers = new HashMap<String, Power>();
        this.rechargablePowers = new HashMap<String, RechargablePower>();
        if (e.element("encounterPowers") != null) {
            Iterator<Element> i = e.element("encounterPowers").elementIterator();
            while (i.hasNext()) {
                Power power = new Power(i.next());
                this.encounterPowers.put(power.getName(), power);
            }
        }

        if (e.element("rechargablePowers") != null) {
            Iterator<Element> i = e.element("rechargablePowers").elementIterator();
            while (i.hasNext()) {
                RechargablePower power = new RechargablePower(i.next());
                this.rechargablePowers.put(power.getName(), power);
            }
        }
    }
//
//    public Element toXmlElement() {
//        Element e = super.toXmlElement();
//        e.setName("npc");
//        e.add(XmlUtil.textElement("isAlly", String.valueOf(isAlly)));
//        e.add(XmlUtil.textElement("hp", String.valueOf(hp)));
//        e.add(XmlUtil.textElement("maxHp", String.valueOf(maxHp)));
//        e.add(XmlUtil.textElement("surge", String.valueOf(surge)));
//        e.add(XmlUtil.textElement("maxSurge", String.valueOf(maxSurge)));
//        e.add(XmlUtil.textElement("ap", String.valueOf(ap)));
//        e.add(XmlUtil.textElement("initMod", String.valueOf(initMod)));
//
//        if (!encounterPowers.isEmpty()) {
//            Element eps = e.addElement("encounterPowers");
//            for (Power power : encounterPowers.values()) {
//                eps.add(power.toXmlElement());
//            }
//        }
//
//        if (!rechargablePowers.isEmpty()) {
//            Element dps = e.addElement("rechargablePowers");
//            for (Power power : rechargablePowers.values()) {
//                dps.add(power.toXmlElement());
//            }
//        }
//        return e;
//    }

//    public void setMaxHp(int maxHp) {
//        this.maxHp = maxHp;
//    }
//
//    public void setMaxSurge(int maxSurge) {
//        this.maxSurge = maxSurge;
//    }
//
//    public void setAp(int ap) {
//        this.ap = ap;
//    }
//
//    public void setInitMod(int initMod) {
//        this.initMod = initMod;
//    }
//
//    public void setAlly(boolean isAlly) {
//        this.isAlly = isAlly;
//    }

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
            if (key.equalsIgnoreCase(name) || AbbreviationUtil.isAbbre(name, key)) {
                return encounterPowers.get(key);
            }
        }
        for (String key : rechargablePowers.keySet()) {
            if (key.equalsIgnoreCase(name) || AbbreviationUtil.isAbbre(name, key)) {
                return rechargablePowers.get(key);
            }
        }
        return null;
    }

    public int getInitMod() {
        return initMod;
    }

    public boolean isAlly() {
        return isAlly;
    }

    
    public int getMaxHp() {
        return maxHp;
    }

    
    public int getMaxSurge() {
        return maxSurge;
    }

    
    public int getAp() {
        return ap;
    }

    
    public Map<String, RechargablePower> getRechargablePowers() {
        return rechargablePowers;
    }

    
    public Map<String, Power> getEncounterPowers() {
        return encounterPowers;
    }
}
