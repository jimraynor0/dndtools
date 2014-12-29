package org.toj.dnd.irctoolkit.game.d6smw;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.game.dnd3r.PC;
import org.toj.dnd.irctoolkit.util.XmlUtil;

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
        Element e = DocumentHelper.createElement("game");
        e.add(XmlUtil.textElement("name", getName()));
        e.add(XmlUtil.textElement("ruleSet", getRuleSet()));
        if (!StringUtils.isEmpty(getDm())) {
            e.add(XmlUtil.textElement("dm", getDm()));
        }

        e.addElement("mechs");
        for (String key : mechs.keySet()) {
            e.element("mechs").add(mechs.get(key).toXmlElement());
        }
        if (getAliases() != null && !getAliases().isEmpty()) {
            e.addElement("aliases");
            for (String key : getAliases().keySet()) {
                Element alias = e.element("aliases").addElement("alias");
                alias.addAttribute("abbr", key);
                alias.addAttribute("text", getAliases().get(key));
            }
        }
        return e;
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
