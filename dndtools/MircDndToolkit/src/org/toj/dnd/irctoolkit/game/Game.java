package org.toj.dnd.irctoolkit.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.battle.Battle;
import org.toj.dnd.irctoolkit.game.battle.State;
import org.toj.dnd.irctoolkit.game.encounter.Encounter;
import org.toj.dnd.irctoolkit.game.encounter.NPC;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Game {

    private String ruleSet;
    private String dm;
    private String name;
    private String outputChan;
    private Map<String, PC> pcs;
    private Battle battle;
    private Map<String, String> aliases;
    private Map<String, Item> items;

    public Game() {
        super();
        pcs = new HashMap<String, PC>();
        aliases = new HashMap<String, String>();
        items = new HashMap<String, Item>();
    }

    public Game(String name) {
        this();
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public Game(Element e) {
        this();
        this.name = e.elementTextTrim("name");
        this.outputChan = e.elementTextTrim("chan");
        if (e.element("dm") != null) {
            dm = e.elementTextTrim("dm");
        }

        if (e.element("pcs") != null) {
            Iterator<Element> i = e.element("pcs").elementIterator();
            while (i.hasNext()) {
                Element pcElement = i.next();
                PC pc = new PC(pcElement);
                this.pcs.put(pc.getName(), pc);
            }
        }

        if (e.element("aliases") != null) {
            Iterator<Element> i = e.element("aliases").elementIterator();
            while (i.hasNext()) {
                Element alias = i.next();
                this.aliases.put(alias.attributeValue("abbr"),
                        alias.attributeValue("text"));
            }
        }

        if (e.element("items") != null) {
            Iterator<Element> i = e.element("items").elementIterator();
            while (i.hasNext()) {
                Item c = new Item(i.next());
                this.items.put(c.getName(), c);
            }
        }

        this.battle = e.element("battle") == null ? null : new Battle(
                e.element("battle"), this.pcs);
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement("game");
        e.add(XmlUtil.textElement("name", name));
        e.add(XmlUtil.textElement("chan", outputChan));
        if (dm != null && !dm.isEmpty()) {
            e.add(XmlUtil.textElement("dm", dm));
        }

        e.addElement("pcs");
        for (String key : pcs.keySet()) {
            e.element("pcs").add(pcs.get(key).toXmlElement());
        }
        if (aliases != null && !aliases.isEmpty()) {
            e.addElement("aliases");
            for (String key : aliases.keySet()) {
                Element alias = e.element("aliases").addElement("alias");
                alias.addAttribute("abbr", key);
                alias.addAttribute("text", aliases.get(key));
            }
        }
        if (items != null && !items.isEmpty()) {
            Element dps = e.addElement("items");
            for (Item item : items.values()) {
                dps.add(item.toXmlElement());
            }
        }
        if (this.battle != null) {
            e.add(battle.toXmlElement());
        }
        return e;
    }

    public String getName() {
        return name;
    }

    public void addPc(String name) {
        pcs.put(name, new PC(name));
    }

    public void removePc(String name) {
        pcs.remove(name);
    }

    public Battle getBattle() {
        return battle;
    }

    public Map<String, String> getAliases() {
        return aliases;
    }

    public String mapAlias(String alias) {
        return aliases.get(alias);
    }

    public boolean hasAliases(String alias) {
        return aliases.containsKey(alias);
    }

    public void addAlias(String alias, String phrase) {
        this.aliases.put(alias, phrase);
    }

    public void removeAlias(String alias) {
        this.aliases.remove(alias);
    }

    public String getStatString(String[] charNames) {
        StringBuilder sb = new StringBuilder();
        for (String name : charNames) {
            PC ch = findCharByNameOrAbbre(name);
            if (ch != null) {
                if (sb.length() != 0) {
                    sb.append("----------\r\n");
                }
                sb.append(ch.toStatString());
            }
        }
        return sb.toString();
    }

    public PC findCharByNameOrAbbre(String name) {
        if (pcs.containsKey(name)) {
            return pcs.get(name);
        } else {
            for (String pcName : pcs.keySet()) {
                if (AbbreviationUtil.isPrefixAbbre(name, pcName)) {
                    return pcs.get(pcName);
                }
            }
        }
        return null;
    }

    public String getStatString() {
        StringBuilder sb = new StringBuilder();
        for (PC pc : pcs.values()) {
            if (sb.length() != 0) {
                sb.append("----------\r\n");
            }
            sb.append(pc.toStatString());
        }
        return sb.toString();
    }

    public void startBattle() {
        if (!inBattle()) {
            this.battle = new Battle();
        }
    }

    public void startEncounter(Encounter encounter) {
        if (!inBattle()) {
            this.battle = new Battle(encounter);
        }
    }

    public boolean inBattle() {
        return battle != null;
    }

    public void endBattle() {
        for (PC ch : pcs.values()) {
            ch.getStates().clear();
            ch.setThp(0);
        }
        this.battle = null;
    }

    public String generateTopic() {
        if (inBattle()) {
            return battle.toString();
        } else {
            return toTopicString();
        }
    }

    private String toTopicString() {
        StringBuilder sb = new StringBuilder().append((char) 3).append(3);
        for (PC ch : pcs.values()) {
            if (sb.length() > 2) {
                sb.append(", ");
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    public void applyState(String charName, String stateStr) {
        if (inBattle()) {
            battle.applyState(charName, stateStr);
        } else {
            PC pc = this.findCharByNameOrAbbre(charName);
            if (pc != null) {
                pc.applyState(State.parseState(stateStr));
            }
        }
    }

    public void removeState(String charName, String stateStr) {
        if (inBattle()) {
            battle.removeState(charName, stateStr);
        } else {
            PC pc = this.findCharByNameOrAbbre(charName);
            if (pc != null) {
                pc.removeState(pc.matchState(State.parseState(stateStr)));
            }
        }
    }

    public void damage(String charName, int dmg) {
        if (inBattle()) {
            battle.damage(charName, dmg);
        } else {
            PC pc = this.findCharByNameOrAbbre(charName);
            if (pc != null) {
                pc.damage(dmg);
            }
        }
    }

    public void heal(String charName, int healValue) {
        if (inBattle()) {
            battle.heal(charName, healValue);
        } else {
            PC pc = this.findCharByNameOrAbbre(charName);
            if (pc != null) {
                pc.heal(healValue);
            }
        }
    }

    public void addThp(String charName, int thp) {
        if (inBattle()) {
            battle.addThp(charName, thp);
        } else {
            PC pc = this.findCharByNameOrAbbre(charName);
            if (pc != null) {
                pc.setThp(thp);
            }
        }
    }

    public void renameChar(String charName, String newName) {
        if (inBattle()) {
            battle.renameChar(charName, newName);
        } else {
            PC pc = this.findCharByNameOrAbbre(charName);
            if (pc != null) {
                pc.rename(newName);
            }
        }
    }

    public Map<String, PC> getPcs() {
        return pcs;
    }

    public Map<String, NPC> getNpcs() {
        return inBattle() ? new HashMap<String, NPC>() : getBattle().getNpcs();
    }

    public String getOutputChannel() {
        return outputChan;
    }

    public void setOutputChannel(String defaultOutputChannel) {
        this.outputChan = defaultOutputChannel;
    }

    public void addPower(String power, String pcName) {
        PC pc = this.findCharByNameOrAbbre(pcName);
        String[] parsed = power.split("\\|");
        String type = parsed[1];
        String name = parsed[0];
        int charges = 1;
        String desc = null;
        if (parsed.length == 3) {
            try {
                charges = Integer.parseInt(parsed[2]);
            } catch (NumberFormatException e) {
                desc = parsed[2];
            }
        }
        if (parsed.length == 4) {
            charges = Integer.parseInt(parsed[2]);
            desc = parsed[3];
        }
        pc.addPower(type, new Power(name, charges, desc));
    }

    public void removePower(String power, String pcName) {
        PC pc = this.findCharByNameOrAbbre(pcName);
        if (power.contains("\\|")) {
            String[] parsed = power.split("\\|");
            pc.removePower(parsed[1], parsed[0]);
        } else {
            pc.removePower(power);
        }
    }

    public void applyShortRest() {
        for (PC pc : pcs.values()) {
            pc.applyShortRest();
        }
    }

    public void applyExtendedRest() {
        for (PC pc : pcs.values()) {
            pc.applyExtendedRest();
        }
    }

    public List<String> readPower(String powerName) {
        List<String> powerRead = new LinkedList<String>();
        for (PC pc : pcs.values()) {
            String powerDesc = pc.readPower(powerName);
            if (powerDesc != null && !powerDesc.isEmpty()) {
                powerRead.add(pc.getName() + ": " + powerDesc);
            }
        }
        return powerRead;
    }

    public void addChar(String name) {
        PC pc = this.findCharByNameOrAbbre(name);
        if (pc != null) {
            battle.addChar(pc);
        } else {
            battle.addChar(name);
        }
    }

    public void addCharByInit(String name, double init) {
        PC pc = this.findCharByNameOrAbbre(name);
        if (pc != null) {
            battle.addCharByInit(pc, init);
        } else {
            battle.addCharByInit(name, init);
        }
    }

    public String removeItem(Item item) {
        if (!items.containsKey(item.getName())) {
            return "团队持有物品中没有[" + item.getName() + "]";
        }
        Item loot = items.get(item.getName());
        if (loot.getCharges() < item.getCharges()) {
            return "团队持有的物品[" + item.getName() + "]数量只有" + loot.getCharges();
        }
        loot.decreaseCharge(item.getCharges());
        if (loot.getCharges() == 0) {
            items.remove(loot.getName());
        }
        return null;
    }

    public void addItem(Item item) {
        if (!items.containsKey(item.getName())) {
            items.put(item.getName(), item);
        } else {
            items.get(item.getName()).increaseCharge(item.getCharges());
        }
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public boolean isDm(String caller) {
        return dm == null || dm.isEmpty() || dm.equals(caller);
    }

    public String getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(String ruleSet) {
        this.ruleSet = ruleSet;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void setItems(Map<String, Item> items) {
        this.items = items;
    }
}