package org.toj.dnd.irctoolkit.game.battle;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.battle.event.BattleEvent;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Combatant implements Cloneable {

    private String name;
    private int wound;
    private int nonlethal;
    private int thp;
    private double init;

    protected LinkedList<State> states;

    private Combatant() {
        this.states = new LinkedList<State>();
    }

    public Combatant(String charName) {
        this();
        this.name = charName;
    }

    public Combatant(String charName, double init) {
        this(charName);
        this.init = init;
    }

    @SuppressWarnings("unchecked")
    public Combatant(Element e) {
        this();
        this.name = e.elementTextTrim("name");
        this.wound = Integer.parseInt(e.elementTextTrim("wound"));
        if (e.element("nonlethal") != null) {
            this.nonlethal = Integer.parseInt(e.elementTextTrim("nonlethal"));
        }
        if (e.element("thp") != null) {
            this.thp = Integer.parseInt(e.elementTextTrim("thp"));
        }
        this.init = Double.parseDouble(e.elementTextTrim("init"));

        if (e.element("states") != null) {
            Iterator<Element> i = e.element("states").elementIterator();
            while (i.hasNext()) {
                this.states.add(State.parseState(i.next().getText()));
            }
        }
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement("combatant");
        e.add(XmlUtil.textElement("name", name));
        e.add(XmlUtil.textElement("wound", String.valueOf(wound)));
        e.add(XmlUtil.textElement("nonlethal", String.valueOf(nonlethal)));
        e.add(XmlUtil.textElement("thp", String.valueOf(thp)));
        e.add(XmlUtil.textElement("init", String.valueOf(init)));

        if (!this.states.isEmpty()) {
            Element states = e.addElement("states");
            for (State state : this.states) {
                states.add(XmlUtil.textElement("state", state.toStatString()));
            }
        }

        return e;
    }

    public void damage(int dmg) {
        if (thp >= dmg) {
            thp -= dmg;
            return;
        }
        if (thp > 0) {
            dmg -= thp;
            thp = 0;
        }
        wound += dmg;
    }

    public void nonlethalDamage(int dmg) {
        nonlethal += dmg;
    }

    public void heal(int heal) {
        if (nonlethal > 0) {
            nonlethal -= heal;
            if (nonlethal < 0) {
                nonlethal = 0;
            }
        }
        wound -= heal;
        if (wound < 0) {
            wound = 0;
        }
    }

    public void setThp(int thp) {
        this.thp = thp;
    }

    public void applyState(State state) {
        states.add(state);
    }

    public void removeState(State state) {
        states.remove(state);
    }

    public State matchState(State state) {
        State match = null;
        int matchFields = 0;
        for (State target : states) {
            if (matchFields < 1 && target.getName().equals(state.getName())) {
                match = target;
                matchFields = 1;
            }
            if (matchFields < 2 && target.getName().equals(state.getName())
                    && target.endConditionMatches(state.getEndCondition())) {
                match = target;
                matchFields = 2;
            }
            if (matchFields < 3 && target.getName().equals(state.getName())
                    && target.endConditionMatches(state.getEndCondition())
                    && target.getAppliedOnInit() == state.getAppliedOnInit()) {
                match = target;
                matchFields = 3;
            }
            if (target.equals(state)) {
                return target;
            }
        }
        return match;
    }

    public String getName() {
        return name;
    }

    public void rename(String name) {
        this.name = name;
    }

    public double getInit() {
        return init;
    }

    public void setInit(double init) {
        this.init = init;
    }

    public String toString(boolean onTurn) {
        StringBuilder sb = new StringBuilder();
        if (onTurn) {
            sb.append(IrcColoringUtil.paint(name, Color.RED.getCode()));
        } else {
            sb.append(name);
        }
        sb.append(getHpExpression());
        if (states.size() > 0) {
            sb.append("(");
            boolean first = true;
            for (State state : states) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(state);
                first = false;
            }
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toOnTurnString() {
        return toString(true);
    }

    protected String getHpExpression() {
        StringBuilder sb = new StringBuilder();
        if (wound > 0) {
            sb.append("-").append(wound);
        }
        if (thp > 0) {
            sb.append("+").append(thp);
        }
        if (nonlethal > 0) {
            sb.append("(-").append(nonlethal).append(")");
        }
        return sb.toString();
    }

    public List<String> checkStateBehavior(BattleEvent e) {
        if (states == null) {
            return null;
        }
        List<String> result = new LinkedList<String>();
        LinkedList<State> statesCopy = new LinkedList<State>();
        statesCopy.addAll(states);
        for (State s : statesCopy) {
            List<String> stateMsgsFromState = s.triggerBehavior(e, this);
            if (stateMsgsFromState != null && !stateMsgsFromState.isEmpty()) {
                result.addAll(stateMsgsFromState);
            }
        }
        return result;
    }

    @Override
    public Combatant clone() {
        try {
            Combatant clone = (Combatant) super.clone();
            clone.states = new LinkedList<State>();
            for (State state : states) {
                clone.states.add(state.clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(init);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((states == null) ? 0 : states.hashCode());
        result = prime * result + thp;
        result = prime * result + wound;
        result = prime * result + nonlethal;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Combatant other = (Combatant) obj;
        if (Double.doubleToLongBits(init) != Double
                .doubleToLongBits(other.init))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (states == null) {
            if (other.states != null)
                return false;
        } else if (!states.equals(other.states))
            return false;
        if (thp != other.thp)
            return false;
        if (wound != other.wound)
            return false;
        if (nonlethal != other.nonlethal)
            return false;
        return true;
    }

    public int getWound() {
        return wound;
    }

    public int getNonlethal() {
        return nonlethal;
    }

    public void setNonlethal(int nonlethal) {
        this.nonlethal = nonlethal;
    }

    public int getThp() {
        return thp;
    }

    public LinkedList<State> getStates() {
        return states;
    }
}
