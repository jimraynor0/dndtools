package org.toj.dnd.irctoolkit.game;

import org.dom4j.Element;

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
