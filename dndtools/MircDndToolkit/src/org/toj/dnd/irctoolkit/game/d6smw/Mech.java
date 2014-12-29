package org.toj.dnd.irctoolkit.game.d6smw;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.State;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Mech {
    private String name;
    private String model;
    private int heat;
    private int heatSink;
    private int speed;
    private String direction;
    private Map<String, Section> sections = new HashMap<String, Section>();
    private Map<String, Equipment> equipments =
        new HashMap<String, Equipment>();
    private Map<String, Ammo> ammoTrackers = new HashMap<String, Ammo>();

    public Mech(Element e) {
        name = e.elementTextTrim("name");
        model = e.elementTextTrim("model");
        if (e.element("heat") != null) {
            heat = Integer.parseInt(e.elementTextTrim("heat"));
        } else {
            heat = 0;
        }
        heatSink = Integer.parseInt(e.elementTextTrim("heatSink"));

        if (e.element("speed") != null) {
            speed = Integer.parseInt(e.elementTextTrim("speed"));
        } else {
            speed = 0;
        }

        if (e.element("direction") != null) {
            direction = e.elementTextTrim("direction");
        }

        if (e.element("ammoTrackers") != null) {
            Iterator<Element> i = e.element("ammoTrackers").elementIterator();
            while (i.hasNext()) {
                Element ammoElement = i.next();
                Ammo a = new Ammo(ammoElement);
                ammoTrackers.put(a.getType(), a);
            }
        }

        Iterator<Element> i = e.element("sections").elementIterator();
        while (i.hasNext()) {
            Element secElement = i.next();
            Section sec = new Section(secElement);
            sections.put(sec.getName(), sec);
            for (Equipment eq : sec.getEquipments().values()) {
                equipments.put(eq.getName(), eq);
                if (eq instanceof Weapon) {
                    ((Weapon) eq).setAmmo(ammoTrackers.get(((Weapon) eq)
                        .getAmmoType()));
                }
            }
        }
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement("mech");
        e.add(XmlUtil.textElement("name", name));
        e.add(XmlUtil.textElement("model", model));
        e.add(XmlUtil.textElement("heat", String.valueOf(heat)));
        e.add(XmlUtil.textElement("heatSink", String.valueOf(heatSink)));
        e.add(XmlUtil.textElement("speed", String.valueOf(speed)));
        e.add(XmlUtil.textElement("direction", direction));

        Element secElement = e.addElement("sections");
        for (Section section : sections.values()) {
            secElement.add(section.toXmlElement());
        }

        Element ammoElement = e.addElement("ammoTrackers");
        for (Ammo ammo : ammoTrackers.values()) {
            ammoElement.add(ammo.toXmlElement());
        }

        return e;
    }

    public String activateEquipment(String equipment, TimePoint activatingOn) {
        String activateResult =
            equipments.get(equipment).activate(activatingOn);
        if (activateResult == null) {
            heat += equipments.get(equipment).getHeat();
        }
        return activateResult;
    }

    public void damage(int dmg, String section) {
        sections.get(section).damage(dmg);
    }

    public void quickRepair(int repair, String section) {
        sections.get(section).fastRepair(repair);
    }

    public void completeRepair() {
        for (Section sec : sections.values()) {
            sec.completeRepair();
        }
    }

    public void repairEquipment(String equipment) {
        equipments.get(equipment).active();
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

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getHeatSink() {
        return heatSink;
    }

    public void setHeatSink(int heatSink) {
        this.heatSink = heatSink;
    }

    public Map<String, Section> getSections() {
        return sections;
    }

    public void setSections(Map<String, Section> sections) {
        this.sections = sections;
    }

    public Map<String, Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(Map<String, Equipment> equipments) {
        this.equipments = equipments;
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

    public Map<String, Ammo> getAmmoTrackers() {
        return ammoTrackers;
    }

    public void setAmmoTrackers(Map<String, Ammo> ammoTrackers) {
        this.ammoTrackers = ammoTrackers;
    }
}
