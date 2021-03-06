package org.toj.dnd.irctoolkit.game.dnd3r;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.util.XmlUtil;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class LimitedResource {
    private String name;
    private String desc;
    private int charges;

    public LimitedResource() {}

    public LimitedResource(String name, int charges) {
        this.name = name;
        this.charges = charges;
    }

    public LimitedResource(String name, String desc, int charges) {
        this(name, charges);
        this.desc = desc;
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

    public void decreaseCharge(int decCharge) {
        this.charges -= decCharge;
    }

    public void increaseCharge(int incCharge) {
        this.charges += incCharge;
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
        Element e = DocumentHelper.createElement(getTypeName());
        e.add(XmlUtil.textElement("name", name));
        if (desc != null) {
            e.add(XmlUtil.textElement("desc", desc));
        }
        e.add(XmlUtil.textElement("charges", String.valueOf(charges)));
        return e;
    }

    protected abstract String getTypeName();
}
