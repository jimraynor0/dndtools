package org.toj.dnd.irctoolkit.game;

import org.dom4j.Element;

public class Spell extends LimitedResource {

    public Spell(Element e) {
        super(e);
    }

    public Spell(String name, int charges) {
        super(name, charges);
    }

    public Spell(String name, String desc, int charges) {
        super(name, desc, charges);
    }

    protected String getTypeName() {
        return "spell";
    }
}
