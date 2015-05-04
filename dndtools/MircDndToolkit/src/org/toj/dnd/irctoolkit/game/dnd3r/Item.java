package org.toj.dnd.irctoolkit.game.dnd3r;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.dom4j.Element;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Item extends LimitedResource {

    public Item(Element e) {
        super(e);
    }

    public Item(String name, int charges) {
        super(name, charges);
    }

    public Item(String name, String desc, int charges) {
        super(name, desc, charges);
    }

    @Override
    protected String getTypeName() {
        return "spell";
    }
}
