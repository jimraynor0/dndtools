package org.toj.dnd.irctoolkit.game.d6smw;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;
import org.toj.dnd.irctoolkit.engine.command.game.d6smw.Mech;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.game.dnd3r.PC;

public class D6smwGame extends Game {
    private Map<String, Mech> mechs = new HashMap<String, Mech>();

    public D6smwGame(String name) {
        setName(name);
    }

    public D6smwGame(Element e) {
        setName(e.elementTextTrim("name"));
        if (e.element("dm") != null) {
            setDm(e.elementTextTrim("dm"));
        }

        if (e.element("aliases") != null) {
            Iterator<Element> i = e.element("aliases").elementIterator();
            while (i.hasNext()) {
                Element alias = i.next();
                getAliases().put(alias.attributeValue("abbr"),
                        alias.attributeValue("text"));
            }
        }

        if (e.element("mechs") != null) {
            Iterator<Element> i = e.element("mechs").elementIterator();
            while (i.hasNext()) {
                Element mechElement = i.next();
                Mech m = new Mech(mechElement);
                this.mechs.put(m.getName(), m);
            }
        }
    }

    @Override
    public Element toXmlElement() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRuleSet() {
        return "d6smw";
    }

    @Override
    public String generateTopic() {
        return null;
    }

    @Override
    public String getGameCommandPackage() {
        return "org.toj.dnd.irctoolkit.engine.command.game.d6smw";
    }
}
