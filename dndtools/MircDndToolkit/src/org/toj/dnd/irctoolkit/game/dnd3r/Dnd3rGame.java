package org.toj.dnd.irctoolkit.game.dnd3r;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.Battle;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.State;
import org.toj.dnd.irctoolkit.game.dnd3r.encounter.Encounter;
import org.toj.dnd.irctoolkit.game.dnd3r.encounter.NPC;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Dnd3rGame extends Game {

    private Map<String, PC> pcs;
    private Battle battle;
    private Map<String, Item> items;

    public Dnd3rGame() {
        super();
        pcs = new HashMap<String, PC>();
        items = new HashMap<String, Item>();
    }

    public Dnd3rGame(String name) {
        this();
        setName(name);
    }

    @SuppressWarnings("unchecked")
    public Dnd3rGame(Element e) {
        this();
        setName(e.elementTextTrim("name"));
        if (e.element("dm") != null) {
            setDm(e.elementTextTrim("dm"));
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
                getAliases().put(alias.attributeValue("abbr"),
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
        e.add(XmlUtil.textElement("name", getName()));
        e.add(XmlUtil.textElement("ruleSet", getRuleSet()));
        if (!StringUtils.isEmpty(getDm())) {
            e.add(XmlUtil.textElement("dm", getDm()));
        }

        e.addElement("pcs");
        for (String key : pcs.keySet()) {
            e.element("pcs").add(pcs.get(key).toXmlElement());
        }
        if (getAliases() != null && !getAliases().isEmpty()) {
            e.addElement("aliases");
            for (String key : getAliases().keySet()) {
                Element alias = e.element("aliases").addElement("alias");
                alias.addAttribute("abbr", key);
                alias.addAttribute("text", getAliases().get(key));
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

    public void addPc(String name) {
        pcs.put(name, new PC(name));
    }

    public void removePc(String name) {
        pcs.remove(name);
    }

    public Battle getBattle() {
        return battle;
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

    public void applyExtendedRest() {
        for (PC pc : pcs.values()) {
            pc.applyExtendedRest();
        }
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

    public Map<String, Item> getItems() {
        return items;
    }

    public void setItems(Map<String, Item> items) {
        this.items = items;
    }

    @Override
    public String getRuleSet() {
        return "dnd3r";
    }

    @Override
    public String getGameCommandPackage() {
        return "org.toj.dnd.irctoolkit.engine.command.game.dnd3r";
    }
}
