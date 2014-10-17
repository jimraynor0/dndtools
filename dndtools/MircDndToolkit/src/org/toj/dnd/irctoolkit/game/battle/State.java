package org.toj.dnd.irctoolkit.game.battle;

import java.util.LinkedList;
import java.util.List;

import org.toj.dnd.irctoolkit.game.battle.behavior.BattleEvent;
import org.toj.dnd.irctoolkit.game.battle.behavior.EndsInAFewTurnsBehavior;
import org.toj.dnd.irctoolkit.game.battle.behavior.FastHealingBehavior;
import org.toj.dnd.irctoolkit.game.battle.behavior.StateBehavior;
import org.toj.dnd.irctoolkit.util.StringNumberUtil;

// TODO multiple instance of same state on same char
public class State implements Cloneable {

    private static final String TYPE_DOT = "dot";
    private static final String TYPE_FAST_HEALING = "fh";

    public static State parseState(String stateStr, int round, double init) {
        String[] stateParams = stateStr.split("\\|");
        State state = null;
        if (stateParams.length == 1) {
            state = new State(stateParams[0].trim(), null, round, init);
        } else if (stateParams.length == 2) {
            state = new State(stateParams[0].trim(), stateParams[1].trim(),
                    round, init);
        } else if (stateParams.length == 3) {
            state = new State(stateParams[0].trim(), stateParams[1].trim(),
                    round, Double.parseDouble(stateParams[2]));
        } else {
            state = new State(stateParams[0].trim(), stateParams[1].trim(),
                    Integer.parseInt(stateParams[2]),
                    Double.parseDouble(stateParams[3]));
        }
        return state;
    }

    public static State parseState(String stateStr) {
        String[] stateParams = stateStr.split("\\|");
        State state = null;
        if (stateParams.length == 1) {
            state = new State(stateParams[0].trim(), null);
        } else if (stateParams.length == 2) {
            state = new State(stateParams[0].trim(), stateParams[1].trim());
        } else {
            state = new State(stateParams[0].trim(), stateParams[1].trim(),
                    Integer.parseInt(stateParams[2]),
                    Double.parseDouble(stateParams[3]));
        }
        return state;
    }

    private String name;
    private String endCondition;
    private int appliedOnRound;
    private double appliedOnInit;

    private LinkedList<StateBehavior> behaviors;

    private State(String name, String endCondition) {
        this.name = name;
        // 4th be with you
        // if (isDot(name)) {
        // this.endCondition = endCondition == null ? END_COND_SAVE
        // : endCondition;
        // String dmg = name.substring(TYPE_DOT.length());
        // getBehaviorList().add(new DotBehavior(Integer.parseInt(dmg), this));
        // } else {
        // this.endCondition = endCondition == null ? END_COND_EONT
        // : endCondition;
        // }
        this.endCondition = endCondition;
        if (endCondition != null) {
            buildEndConditionBehavior();
        }
        if (isFastHealing(name)) {
            String heal = name.substring(TYPE_FAST_HEALING.length());
            getBehaviorList().add(
                    new FastHealingBehavior(Integer.parseInt(heal), this));
        }
    }

    private State(String name, String endCondition, int round, double init) {
        this.name = name;
        this.appliedOnInit = init;
        this.appliedOnRound = round;

        // 4th be with you
        // if (isDot(name)) {
        // this.endCondition =
        // endCondition == null ? END_COND_SAVE : endCondition;
        // String dmg = name.substring(TYPE_DOT.length());
        // getBehaviorList().add(new DotBehavior(Integer.parseInt(dmg), this));
        // } else {
        // this.endCondition =
        // endCondition == null ? END_COND_EONT : endCondition;
        // }
        this.endCondition = endCondition;
        if (endCondition != null) {
            buildEndConditionBehavior();
        }
        if (isFastHealing(name)) {
            String heal = name.substring(TYPE_FAST_HEALING.length());
            getBehaviorList().add(
                    new FastHealingBehavior(Integer.parseInt(heal), this));
        }
    }

    private void buildEndConditionBehavior() {
        if (StringNumberUtil.isInteger(endCondition)) {
            getBehaviorList().add(new EndsInAFewTurnsBehavior(this));
        }
    }

    private boolean isFastHealing(String name) {
        return name.toLowerCase().startsWith(TYPE_FAST_HEALING);
    }

    private LinkedList<StateBehavior> getBehaviorList() {
        if (behaviors == null) {
            behaviors = new LinkedList<StateBehavior>();
        }
        return behaviors;
    }

    public String getName() {
        return name;
    }

    public String getEndCondition() {
        return endCondition;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (endCondition != null && !endCondition.isEmpty()) {
            sb.append("|").append(endCondition);
        }
        return sb.toString();
    }

    public String toStatString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("|")
                .append(endCondition == null ? "" : endCondition).append("|")
                .append(appliedOnRound).append("|").append(this.appliedOnInit);
        return sb.toString();
    }

    public List<String> triggerBehavior(BattleEvent e, Combatant owner) {
        if (this.behaviors == null) {
            return null;
        }
        List<String> result = new LinkedList<String>();
        for (StateBehavior b : this.behaviors) {
            String msg = b.onTurnStart(e.getRound(), e.getInit(), owner);
            if (msg != null) {
                result.add(msg);
            }
            msg = b.onTurnEnd(e.getRound(), e.getInit(), owner);
            if (msg != null) {
                result.add(msg);
            }
        }
        return result;
    }

    public int getAppliedOnRound() {
        return appliedOnRound;
    }

    public double getAppliedOnInit() {
        return appliedOnInit;
    }

    public void setEndCondition(String endCondition) {
        this.endCondition = endCondition;
    }

    public State clone() {
        try {
            // for now it's ok to shallow copy behavior list since they are only
            // strategy classes.
            return (State) super.clone();
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
        temp = Double.doubleToLongBits(appliedOnInit);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + appliedOnRound;
        result = prime * result
                + ((endCondition == null) ? 0 : endCondition.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        State other = (State) obj;
        if (Double.doubleToLongBits(appliedOnInit) != Double
                .doubleToLongBits(other.appliedOnInit))
            return false;
        if (appliedOnRound != other.appliedOnRound)
            return false;
        if (endCondition == null) {
            if (other.endCondition != null)
                return false;
        } else if (!endCondition.equals(other.endCondition))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public boolean endConditionMatches(String other) {
        if (endCondition == null) {
            if (other != null)
                return false;
        } else if (!endCondition.equals(other))
            return false;
        return true;
    }
}
