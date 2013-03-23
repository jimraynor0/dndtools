package org.toj.dnd.irctoolkit.game.encounter.powerrecharger;

import org.toj.dnd.irctoolkit.game.encounter.RechargablePower;

public abstract class PowerRecharger {
    private static final String DICE_PREFIX = "dice:";
    private static final String BLOODIED = "bloodied";
    private RechargablePower controlls;

    public static PowerRecharger condition(String cond, RechargablePower power) {
        // if (cond.toLowerCase().startsWith(DICE_PREFIX)) {
        // return new D6OnStartOfTurnRecharger(cond, power);
        // }
        // if (cond.toLowerCase().startsWith(DICE_PREFIX)) {
        // return new OnBloodiedRecharger(power);
        // }
        return null;
    }

    protected boolean recharge() {
        if (controlls.getCharges() < controlls.getMaxCharges()) {
            controlls.setCharges(controlls.getCharges() + 1);
            return true;
        }
        return false;
    }
    //
    // private String trigger;
    // private String cond;
    //
    // public RechargeCondition(String param) {
    // this(param.split("\\|")[0], param.split("\\|")[1]);
    // }
    //
    // public RechargeCondition(String trigger, String cond) {
    // this.trigger = trigger;
    // this.cond = cond;
    // }
    //
    // public boolean triggersOn(String trigger) {
    // return this.trigger.equalsIgnoreCase(trigger);
    // }
    //
    // public boolean meets(String cond) {
    // return this.cond.equalsIgnoreCase(cond);
    // }
}