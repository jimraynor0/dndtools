package org.toj.dnd.irctoolkit.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class PC extends Combatant {

    private int xp;
    private int hp;
    private int maxHp;
    private int pp;
    private int maxPp;
    private int initMod;
    private Map<String, Item> items;
    private Map<String, List<Spell>> spells;

    public PC(String name) {
        super(name);
        this.items = new HashMap<String, Item>();
        this.spells = new HashMap<String, List<Spell>>();
    }

    @SuppressWarnings("unchecked")
    public PC(Element e) {
        super(e);
        this.xp = Integer.parseInt(e.elementTextTrim("xp"));
        this.hp = Integer.parseInt(e.elementTextTrim("hp"));
        this.maxHp = Integer.parseInt(e.elementTextTrim("maxHp"));
        if (e.element("maxPp") != null) {
            this.pp = Integer.parseInt(e.elementTextTrim("pp"));
            this.maxPp = Integer.parseInt(e.elementTextTrim("maxPp"));
        }
        String initModSave = e.elementTextTrim("initMod");
        if (initModSave != null) {
            this.initMod = Integer.parseInt(initModSave);
        }

        this.items = new HashMap<String, Item>();
        if (e.element("items") != null) {
            Iterator<Element> i = e.element("items").elementIterator();
            while (i.hasNext()) {
                Item c = new Item(i.next());
                this.items.put(c.getName(), c);
            }
        }

        this.spells = new HashMap<String, List<Spell>>();
        if (e.element("spells") != null) {
            Iterator<Element> i = e.element("spells").elementIterator();
            while (i.hasNext()) {
                Element groupElement = i.next();

                Iterator<Element> groups = groupElement.elementIterator();
                List<Spell> spellGroup = new ArrayList<Spell>();
                while (groups.hasNext()) {
                    Spell c = new Spell(groups.next());
                    spellGroup.add(c);
                }
                if (!spellGroup.isEmpty()) {
                    this.spells.put(groupElement.attributeValue("name"),
                            spellGroup);
                }
            }
        }
    }

    public void damage(int dmg) {
        if (getThp() >= dmg) {
            setThp(getThp() - dmg);
            return;
        }
        if (getThp() > 0) {
            dmg -= getThp();
            setThp(0);
        }
        hp -= dmg;
    }

    public void heal(int heal) {
        if (getNonlethal() > 0) {
            setNonlethal(getNonlethal() - heal);
            if (getNonlethal() < 0) {
                setNonlethal(0);
            }
        }
        hp += heal;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    public void recordPp(int usage) {
        this.pp -= usage;
        if (pp < 0) {
            pp = 0;
        }
    }

    public void applyExtendedRest() {
        this.spells.clear();
    }

    public String toStatString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PC: ").append(this.getName()).append("\r\n");
        sb.append("XP: ").append(xp).append("\r\n");
        sb.append("HP: ").append(hp).append("/").append(maxHp);
        if (getThp() > 0) {
            sb.append("(临时HP: ").append(this.getThp()).append(")");
        }
        if (getNonlethal() > 0) {
            sb.append("(淤伤: ").append(this.getNonlethal()).append(")");
        }
        sb.append("\r\n");
        if (isPsionic()) {
            sb.append("灵能点: ").append(pp).append("/").append(maxPp)
                    .append("\r\n");
        }
        sb.append("先攻调整值: ").append(this.initMod).append("\r\n");
        if (states != null && !states.isEmpty()) {
            sb.append("状态: ");
            for (State s : states) {
                sb.append(s.toString());
                sb.append(s != states.getLast() ? ", " : "\r\n");
            }
        }
        return sb.toString();
    }

    public Element toXmlElement() {
        Element e = super.toXmlElement();
        e.setName("pc");
        e.add(XmlUtil.textElement("xp", String.valueOf(xp)));
        e.add(XmlUtil.textElement("hp", String.valueOf(hp)));
        e.add(XmlUtil.textElement("maxHp", String.valueOf(maxHp)));
        if (isPsionic()) {
            e.add(XmlUtil.textElement("pp", String.valueOf(pp)));
            e.add(XmlUtil.textElement("maxPp", String.valueOf(maxPp)));
        }
        e.add(XmlUtil.textElement("initMod", String.valueOf(initMod)));

        if (!items.isEmpty()) {
            Element dps = e.addElement("items");
            for (Item item : items.values()) {
                dps.add(item.toXmlElement());
            }
        }

        if (!spells.isEmpty()) {
            Element dps = e.addElement("spells");
            for (String groupName : spells.keySet()) {
                Element groupEle = dps.addElement("group");
                groupEle.addAttribute("name", groupName);
                for (Spell spell : spells.get(groupName)) {
                    groupEle.add(spell.toXmlElement());
                }
            }
        }
        return e;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public void setMaxPp(int maxPp) {
        this.maxPp = maxPp;
    }

    public String removeItem(Item item) {
        if (!items.containsKey(item.getName())) {
            return this.getName() + "持有物品中没有[" + item.getName() + "]";
        }
        Item loot = items.get(item.getName());
        if (loot.getCharges() < item.getCharges()) {
            return this.getName() + "持有的物品[" + item.getName() + "]数量只有"
                    + loot.getCharges();
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

    public String removeSpell(Spell spell) {
        Spell prepared = findSpell(spell.getName());
        if (prepared == null) {
            return "你没有准备[" + spell.getName() + "]法术";
        }
        if (prepared.getCharges() < spell.getCharges()) {
            return "你只准备了" + prepared.getCharges() + "个[" + spell.getName()
                    + "]法术";
        }
        prepared.decreaseCharge(spell.getCharges());
        if (prepared.getCharges() == 0) {
            for (String group : spells.keySet()) {
                if (spells.get(group).contains(prepared)) {
                    spells.get(group).remove(prepared);
                    if (spells.get(group).isEmpty()) {
                        spells.remove(group);
                    }
                }
            }
        }
        return null;
    }

    public void addSpell(String group, Spell spell) {
        Spell prepared = findSpell(spell.getName());
        if (prepared == null) {
            if (!spells.containsKey(group)) {
                spells.put(group, new ArrayList<Spell>());
            }
            spells.get(group).add(spell);
        } else {
            prepared.increaseCharge(spell.getCharges());
        }
    }

    private Spell findSpell(String name) {
        for (List<Spell> spellGroup : spells.values()) {
            for (Spell spell : spellGroup) {
                if (name.equals(spell.getName())) {
                    return spell;
                }
            }
        }
        return null;
    }

    protected String getHpExpression() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(hp).append("/").append(maxHp);
        if (getThp() > 0) {
            sb.append("+").append(getThp());
        }
        if (getNonlethal() > 0) {
            sb.append("(-").append(getNonlethal()).append(")");
        }
        sb.append(")");
        return sb.toString();
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getInitMod() {
        return initMod;
    }

    public void setInitMod(int initMod) {
        this.initMod = initMod;
    }

    public boolean isPsionic() {
        return this.maxPp > 0;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void setItems(Map<String, Item> consumables) {
        this.items = consumables;
    }

    public Map<String, List<Spell>> getSpells() {
        return spells;
    }

    public void setSpells(Map<String, List<Spell>> spells) {
        this.spells = spells;
    }
}
