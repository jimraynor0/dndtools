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
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class D6smwGame extends Game {
    private Map<String, Mech> mechs = new HashMap<String, Mech>();
    private LinkedList<Mech> mechsInBattle = new LinkedList<Mech>();
    private Mech current;
    private int round = -1;

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
    public String generateTopic() {
        if (!mechsInBattle.isEmpty()) {
            return generateBattleTopic();
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

    private String generateBattleTopic() {
        StringBuilder sb = new StringBuilder();
        sb.append("r").append(round).append(": ");

        for (Mech m : mechsInBattle) {
            if (!m.equals(mechsInBattle.getFirst())) {
                sb.append(", ");
            }
            sb.append(m.toTopicString(m.equals(current)));
        }
        return sb.toString();
    }

    public Mech getMech(String m) {
        return mechs.get(m);
    }

    public void addMechToBattle(Mech m, int init) {
        m.setInit(init);
        mechsInBattle.add(m);
        sortMechsInBattle();
    }

    private void sortMechsInBattle() {
        Collections.sort(mechsInBattle, new Comparator<Mech>() {
            @Override
            public int compare(Mech m1, Mech m2) {
                return m2.getInit() - m1.getInit();
            }
        });
    }

    public void removeMechFromBattle(Mech mech) {
        if (current == mech) {
            end();
        }
        mechsInBattle.remove(mech);
    }

    public void endBattle() {
        for (Mech mech : mechsInBattle) {
            mech.endBattle();
        }
        mechsInBattle.clear();
        round = 0;
        current = null;
    }

    public boolean inBattle() {
        return mechsInBattle != null && !mechsInBattle.isEmpty();
    }

    public void startBattle() {
        round = 1;
    }

    public void end() {
        if (current == null || mechsInBattle == null || mechsInBattle.isEmpty()) {
            return;
        }
        if (current == mechsInBattle.getLast()) {
            this.go(mechsInBattle.getFirst(), round + 1);
        } else {
            this.go(mechsInBattle.get(mechsInBattle.indexOf(current) + 1),
                    round);
        }
    }

    public void go(Mech m, int round) {
        // fire battle event
        if (round >= 0 && m != null && current != null) {
            fireInitiativeChange(round, m.getInit());
        }
        this.round = round;
        if (round < 0) {
            current = null;
        } else {
            current = m;
        }
    }

    private void fireInitiativeChange(int newRound, double newInit) {
        int oldRound = round;
        double oldInit = current.getInit();
        for (Mech m : mechsInBattle) {
            m.sinkHeat(calcInitPassesTimes(new InitiativePassesEvent(oldRound,
                    oldInit, newRound, newInit), m.getInit()));
        }
    }

    private int calcInitPassesTimes(InitiativePassesEvent e, int init) {
        int triggeredTimes = e.getRound() - e.getCurrentRound();

        if (e.getCurrentInit() > init && e.getInit() <= init) {
            triggeredTimes++;
        }
        if (e.getCurrentInit() <= init && e.getInit() > init) {
            triggeredTimes--;
        }

        return triggeredTimes;
    }

    private TimePoint getCurrentTimePoint() {
        TimePoint currentTp = null;
        if (round > 0 && current != null) {
            currentTp = new TimePoint(round, current.getInit());
        }
        return currentTp;
    }

    public void damage(String name, String section, int value) {
        getMech(name).damage(value, section);
    }

    public void repair(String name, String section, int value) {
        getMech(name).quickRepair(value, section);
    }

    public String activateEquipment(Mech mech, String eq) {
        return mech.activateEquipment(eq, getCurrentTimePoint());
    }

    @Override
    public String getRuleSet() {
        return "d6smw";
    }

    @Override
    public String getGameCommandPackage() {
        return "org.toj.dnd.irctoolkit.engine.command.game.d6smw";
    }

    public Mech getCurrent() {
        return current;
    }

    public void setCurrent(Mech current) {
        this.current = current;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Map<String, Mech> getMechs() {
        return mechs;
    }

    public void setMechs(Map<String, Mech> mechs) {
        this.mechs = mechs;
    }

    public LinkedList<Mech> getMechsInBattle() {
        return mechsInBattle;
    }

    public void setMechsInBattle(LinkedList<Mech> mechsInBattle) {
        this.mechsInBattle = mechsInBattle;
    }
}
