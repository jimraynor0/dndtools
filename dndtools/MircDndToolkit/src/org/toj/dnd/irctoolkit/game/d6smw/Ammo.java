package org.toj.dnd.irctoolkit.game.d6smw;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Ammo {
    public String type;
    public int rounds;

    public Ammo(Element e) {
        type = e.elementTextTrim("type");
        rounds = Integer.parseInt(e.elementTextTrim("rounds"));
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement("ammo");
        e.add(XmlUtil.textElement("type", type));
        e.add(XmlUtil.textElement("rounds", String.valueOf(rounds)));
        return e;
    }

    public boolean hasAmmo() {
        return rounds > 0;
    }

    public void load() {
        rounds--;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
}
