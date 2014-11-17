package org.toj.dnd.irctoolkit.game.encounter;

import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.battle.Combatant;
import org.toj.dnd.irctoolkit.game.battle.State;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class NPC extends Combatant {

    private boolean isAlly;
    private int hp;
    private int maxHp;
    private int surge;
    private int maxSurge;
    private int ap;
    private int initMod;

    public NPC(String name) {
        super(name);
    }

    @SuppressWarnings("unchecked")
    public NPC(Element e) {
        super(e);
        this.isAlly = Boolean.parseBoolean(e.elementTextTrim("isAlly"));
        this.hp = Integer.parseInt(e.elementTextTrim("hp"));
        this.maxHp = Integer.parseInt(e.elementTextTrim("maxHp"));
        this.maxSurge = Integer.parseInt(e.elementTextTrim("maxSurge"));
        this.surge = Integer.parseInt(e.elementTextTrim("surge"));
        this.ap = Integer.parseInt(e.elementTextTrim("ap"));
        String initModSave = e.elementTextTrim("initMod");
        if (initModSave != null) {
            this.initMod = Integer.parseInt(initModSave);
        }
    }

    public NPC(Element combatant, Encounter encounter) {
        this(combatant);
    }

    public NPC(String name, NpcTemplate npcTemplate) {
        this(name);
    }

    @Override
    public void damage(int dmg) {
        boolean bloodied = isBloodied();
        if (getThp() >= dmg) {
            setThp(getThp() - dmg);
            return;
        }
        if (getThp() > 0) {
            dmg -= getThp();
            setThp(0);
        }
        hp -= dmg;
        if (!bloodied && isBloodied()) {
            super.applyState(State.parseState("bloodied|eoe"));
        }
    }

    @Override
    public void heal(int heal) {
        boolean bloodied = isBloodied();
        if (hp < 0) {
            hp = 0;
        }
        hp += heal;
        if (hp > maxHp) {
            hp = maxHp;
        }
        if (bloodied && !isBloodied()) {
            super.removeState(State.parseState("bloodied|eoe"));
        }
    }

    public boolean isBloodied() {
        return hp <= maxHp / 2;
    }

    public void recordAp(int usage) {
        this.ap -= usage;
        if (this.ap < 0) {
            this.ap = 0;
        }
    }

    public void recordSurge(int usage) {
        this.surge -= usage;
        if (surge < 0) {
            surge = 0;
        }
    }

    public void applyMilestone() {
        ap++;
    }

    public String toStatString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NPC: ").append(this.getName()).append("\r\n");
        sb.append("Is Ally: ").append(isAlly).append("\r\n");
        sb.append("HP: ").append(hp).append("/").append(maxHp);
        if (getThp() > 0) {
            sb.append("(Temporary HP: ").append(this.getThp()).append(")");
        }
        sb.append("\r\n");
        sb.append("Surge: ").append(surge).append("/").append(maxSurge)
                .append("\r\n");
        sb.append("AP: ").append(this.ap).append("\r\n");
        sb.append("Init Modifier: ").append(this.initMod).append("\r\n");
        if (states != null && !states.isEmpty()) {
            sb.append("Existing Effects: ");
            for (State s : states) {
                sb.append(s.toString());
                sb.append(s != states.getLast() ? ", " : "\r\n");
            }
        }
        return sb.toString();
    }

    @Override
    public Element toXmlElement() {
        Element e = super.toXmlElement();
        e.setName("npc");
        e.add(XmlUtil.textElement("isAlly", String.valueOf(isAlly)));
        e.add(XmlUtil.textElement("hp", String.valueOf(hp)));
        e.add(XmlUtil.textElement("maxHp", String.valueOf(maxHp)));
        e.add(XmlUtil.textElement("surge", String.valueOf(surge)));
        e.add(XmlUtil.textElement("maxSurge", String.valueOf(maxSurge)));
        e.add(XmlUtil.textElement("ap", String.valueOf(ap)));
        e.add(XmlUtil.textElement("initMod", String.valueOf(initMod)));
        return e;
    }

    public void setHp(int hp) {
        boolean bloodied = isBloodied();
        this.hp = hp;
        if (!bloodied && isBloodied()) {
            super.applyState(State.parseState("bloodied|eoe"));
        }
        if (bloodied && !isBloodied()) {
            super.removeState(State.parseState("bloodied|eoe"));
        }
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setSurge(int surge) {
        this.surge = surge;
    }

    public void setMaxSurge(int maxSurge) {
        this.maxSurge = maxSurge;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

    @Override
    protected String getHpExpression() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(hp).append("/").append(maxHp);
        if (getThp() > 0) {
            sb.append("+").append(getThp());
        }
        sb.append(")");
        if (isBloodied()) {
            return IrcColoringUtil.paint(sb.toString(), Color.RED.getCode());
        }
        return sb.toString();
    }

    public int getInitMod() {
        return initMod;
    }

    public void setInitMod(int initMod) {
        this.initMod = initMod;
    }

    public boolean isAlly() {
        return isAlly;
    }

    public void setAlly(boolean isAlly) {
        this.isAlly = isAlly;
    }
}
