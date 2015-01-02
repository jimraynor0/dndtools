package org.toj.dnd.irctoolkit.game.d6smw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.event.InitiativePassesEvent;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class D6smwGame extends Game {
    private Map<String, Mech> mechs = new HashMap<String, Mech>();
    private Battle battle;

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

        if (e.element("battle") != null) {
            battle = new Battle(e.element("battle"), mechs);
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
        if (battle != null) {
            e.add(battle.toXmlElement());
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
    public String generateTopic() {
        if (inBattle()) {
            return battle.generateTopic();
        }

        if (mechs.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        List<String> pcNames = new ArrayList<String>(mechs.keySet());
        Collections.sort(pcNames);
        for (String name : pcNames) {
            if (!name.equals(pcNames.get(0))) {
                sb.append(", ");
            }
            sb.append(mechs.get(name).toTopicString(false));
        }
        return sb.toString();
    }

    public List<String> generateStatString(Mech mech) {
        return mech.toFullStatString(getCurrentTimePoint());
    }

    public List<String> generateStatString() {
        if (mechs.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> stats = new LinkedList<String>();

        List<String> pcNames = new ArrayList<String>(mechs.keySet());
        Collections.sort(pcNames);
        TimePoint currentTp = getCurrentTimePoint();
        for (String name : pcNames) {
            stats.addAll(mechs.get(name).toFullStatString(currentTp));
        }
        return stats;
    }

    private TimePoint getCurrentTimePoint() {
        if (inBattle()) {
            return battle.getCurrentTimePoint();
        }
        return null;
    }

    public Mech getMech(String name) {
        if (mechs.containsKey(name)) {
            return mechs.get(name);
        } else {
            for (String pcName : mechs.keySet()) {
                if (AbbreviationUtil.isPrefixAbbre(name, pcName)) {
                    return mechs.get(pcName);
                }
            }
        }

        return null;
    }

    public void endBattle() {
        for (Mech mech : mechs.values()) {
            mech.endBattle();
        }
        battle = null;
    }

    public boolean inBattle() {
        return battle != null;
    }

    public void startBattle() {
        battle = new Battle();
    }

    public void repair(String name, String section, int value) {
        getMech(name).quickRepair(value, section);
    }

    @Override
    public String getRuleSet() {
        return "d6smw";
    }

    @Override
    public String getGameCommandPackage() {
        return "org.toj.dnd.irctoolkit.engine.command.game.d6smw";
    }

    public Map<String, Mech> getMechs() {
        return mechs;
    }

    public void setMechs(Map<String, Mech> mechs) {
        this.mechs = mechs;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }
}
