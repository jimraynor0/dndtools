package org.toj.dnd.irctoolkit.game.d6smw;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Section {
    private String name;
    private int armor;
    private int hp;
    private int currentMaxHp;
    private int maxHp;
    private Map<String, Equipment> equipments = new HashMap<String, Equipment>();

    public Section(Element e) {
        name = e.elementTextTrim("name");
        if (e.element("armor") != null) {
            armor = Integer.parseInt(e.elementTextTrim("armor"));
        } else {
            armor = 0;
        }
        maxHp = Integer.parseInt(e.elementTextTrim("maxHp"));
        if (e.element("currentMaxHp") != null) {
            currentMaxHp = Integer.parseInt(e.elementTextTrim("currentMaxHp"));
        } else {
            currentMaxHp = maxHp;
        }
        if (e.element("hp") != null) {
            hp = Integer.parseInt(e.elementTextTrim("hp"));
        } else {
            hp = currentMaxHp;
        }

        if (e.element("equipments") != null) {
            Iterator<Element> i = e.element("equipments").elementIterator();
            while (i.hasNext()) {
                Element eqElement = i.next();
                Equipment eq = new Equipment(eqElement);
                equipments.put(eq.getName(), eq);
            }
        }

        if (e.element("weapons") != null) {
            Iterator<Element> i = e.element("weapons").elementIterator();
            while (i.hasNext()) {
                Element wElement = i.next();
                Weapon w = new Weapon(wElement);
                equipments.put(w.getName(), w);
            }
        }
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement("section");
        e.add(XmlUtil.textElement("name", name));
        e.add(XmlUtil.textElement("armor", String.valueOf(armor)));
        e.add(XmlUtil.textElement("hp", String.valueOf(hp)));
        e.add(XmlUtil.textElement("currentMaxHp", String.valueOf(currentMaxHp)));
        e.add(XmlUtil.textElement("maxHp", String.valueOf(maxHp)));

        if (!equipments.isEmpty()) {
            Element eqElement = e.addElement("equipments");
            Element wElement = e.addElement("weapons");
            for (Equipment eq : equipments.values()) {
                if (eq instanceof Weapon) {
                    wElement.add(eq.toXmlElement());
                } else {
                    eqElement.add(eq.toXmlElement());
                }
            }
        }

        return e;
    }

    public void damage(int dmg) {
        dmg -= armor;
        if (dmg <= 0) {
            return;
        }
        hp -= dmg;
        if (hp <= 0) {
            hp = 0;
            deactiveEquipments();
        }
    }

    private void deactiveEquipments() {
        for (Equipment e : equipments.values()) {
            e.deactive();
        }
    }

    public void quickRepair(int repair) {
        hp += repair;
        if (hp > currentMaxHp) {
            hp = currentMaxHp;
        }
        currentMaxHp = hp;
    }

    public void completeRepair() {
        hp = maxHp;
        currentMaxHp = maxHp;
    }

    public String toFullStatString(TimePoint current) {
        StringBuilder sb = new StringBuilder(name);
        sb.append("(").append(hp).append("/").append(currentMaxHp).append("/")
                .append(maxHp).append(")");
        sb.append(" - ");
        boolean first = true;
        for (Equipment eq : equipments.values()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(eq.toFullStatString(current));
        }
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getCurrentMaxHp() {
        return currentMaxHp;
    }

    public void setCurrentMaxHp(int currentMaxHp) {
        this.currentMaxHp = currentMaxHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public Map<String, Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(Map<String, Equipment> equipments) {
        this.equipments = equipments;
    }
}
