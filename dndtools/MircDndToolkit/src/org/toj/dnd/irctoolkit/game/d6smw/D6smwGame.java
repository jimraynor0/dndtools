package org.toj.dnd.irctoolkit.game.d6smw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class D6smwGame extends Game {
    private Map<String, Mech> mechs = new HashMap<String, Mech>();
    private Battle battle;

    public D6smwGame(String name) {
        setName(name);
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
