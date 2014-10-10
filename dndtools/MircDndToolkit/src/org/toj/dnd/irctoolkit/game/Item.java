package org.toj.dnd.irctoolkit.game;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Item {
    private String name;
    private String desc;
    private int charges;

    public Item(String name, int charges) {
        this.name = name;
        this.charges = charges;
    }

    public Item(String name, String desc, int charges) {
        this(name, charges);
        this.desc = desc;
    }

    public Item(Element e) {
        super();
        this.name = e.elementTextTrim("name");
        if (e.element("desc") != null) {
            this.desc = e.elementTextTrim("desc");
        }
        this.charges = Integer.parseInt(e.elementTextTrim("charges"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public void addCharge(int amount) {
        charges += amount;
    }

    public void consume(int amount) {
        charges -= amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        if (this.charges == 1) {
            return name;
        } else {
            return name + "(" + charges + ")";
        }
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement("item");
        e.add(XmlUtil.textElement("name", name));
        if (desc != null) {
            e.add(XmlUtil.textElement("desc", desc));
        }
        e.add(XmlUtil.textElement("charges", String.valueOf(charges)));
        return e;
    }

    public void decreaseCharge(int decCharge) {
        this.charges -= decCharge;
    }

    public void increaseCharge(int incCharge) {
        this.charges += incCharge;
    }
}
