package org.toj.dnd.irctoolkit.game.sr5e;

import org.toj.dnd.irctoolkit.game.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Sr5eGame extends Game {
    private Map<String, PC> pcs = new HashMap<>();
    private Battle battle;

    public Sr5eGame() {}

    public Sr5eGame(String name) {
        super.setName(name);
    }

    public void addPc(String name) {
        PC pc = new PC();
        pc.setName(name);
        pcs.put(name, pc);
    }

    public void removePc(String name) {
        pcs.remove(name);
        if (isInBattle()) {
            battle.remove(name);
        }
    }

    public void renamePc(String oldName, String newName) {
        PC pc = pcs.remove(oldName);
        pc.setName(newName);
        pcs.put(newName, pc);
    }

    public PC getPc(String name) {
        return pcs.get(name);
    }

    public Battle getBattle() {
        return battle;
    }

    public void startBattle() {
        battle = new Battle();
        pcs.values().stream().forEach(PC::startBattle);
    }

    public void endBattle() {
        battle = null;
        pcs.values().stream().forEach(PC::endBattle);
    }

    public boolean isInBattle() {
        return battle == null;
    }

    public boolean isPc(String ch) {
        return pcs.containsKey(ch);
    }

    public Combatant findCharByNameOrAbbre(String name) {
        Optional<PC> pc = pcs.keySet().stream().filter(n -> n.startsWith(name)).findFirst().map(pcs::get);
        if (pc.isPresent()) {
            return pc.get();
        } else if (isInBattle()) {
            return battle.findCharByNameOrAbbre(name);
        }
        return null;
    }

    @Override
    public String getRuleSet() {
        return "sr5e";
    }

    @Override
    public String generateTopic() {
        if (isInBattle()) {
            return battle.generateTopic();
        }
        return pcs.keySet().stream().sorted().map(key -> pcs.get(key)).map(pc -> pc.toTopicString(false))
                .collect(Collectors.joining(", "));
    }

    @Override
    public String getGameCommandPackage() {
        return "org.toj.dnd.irctoolkit.engine.command.game.sr5e";
    }
}
