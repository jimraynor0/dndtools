package org.toj.dnd.irctoolkit.game.dnd3r;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.toj.dnd.irctoolkit.game.dnd3r.battle.Combatant;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.State;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class PC extends Combatant {

    private int xp;
    private int hp;
    private int maxHp;
    private int pp;
    private int maxPp;
    private int initMod;
    private Map<String, Item> items;
    private Map<String, LinkedList<Spell>> spells;

    public PC() {
        super();
        this.items = new HashMap<String, Item>();
        this.spells = new HashMap<String, LinkedList<Spell>>();
    }

    public PC(String name) {
        super(name);
        this.items = new HashMap<String, Item>();
        this.spells = new HashMap<String, LinkedList<Spell>>();
    }

    @Override
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

    @Override
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
                spells.put(group, new LinkedList<Spell>());
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

    @Override
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

    public Map<String, LinkedList<Spell>> getSpells() {
        return spells;
    }

    public void setSpells(Map<String, LinkedList<Spell>> spells) {
        this.spells = spells;
    }
}
