package org.toj.dnd.irctoolkit.game.dnd3r;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Spell extends LimitedResource {

    public Spell() {
        super();
    }

    public Spell(String name, int charges) {
        super(name, charges);
    }

    public Spell(String name, String desc, int charges) {
        super(name, desc, charges);
    }

    @Override
    protected String getTypeName() {
        return "spell";
    }
}
