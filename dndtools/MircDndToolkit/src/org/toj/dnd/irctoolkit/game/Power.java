package org.toj.dnd.irctoolkit.game;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Power {

    private String name;
    private String description;
    private int charges;
    private int maxCharges;
    private String group;

    public Power(String name, int charges, String desc) {
        super();
        this.name = name;
        this.charges = charges;
        this.maxCharges = charges;
        this.description = desc;
    }

    public Power(Element e) {
        super();
        this.name = e.elementTextTrim("name");
        this.description = e.elementTextTrim("description");
        this.charges = Integer.parseInt(e.elementTextTrim("charges"));
        this.maxCharges = Integer.parseInt(e.elementTextTrim("maxCharges"));
        String group = e.elementTextTrim("group");
        if (group != null && !group.isEmpty()) {
            this.group = group;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public int getMaxCharges() {
        return maxCharges;
    }

    public void setMaxCharges(int maxCharges) {
        this.maxCharges = maxCharges;
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement("power");
        e.add(XmlUtil.textElement("name", name));
        e.add(XmlUtil.textElement("description", description));
        e.add(XmlUtil.textElement("charges", String.valueOf(charges)));
        e.add(XmlUtil.textElement("maxCharges", String.valueOf(maxCharges)));
        if (group != null) {
            e.add(XmlUtil.textElement("group", group));
        }
        return e;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
