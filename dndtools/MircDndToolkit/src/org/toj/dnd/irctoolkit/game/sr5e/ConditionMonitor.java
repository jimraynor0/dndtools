package org.toj.dnd.irctoolkit.game.sr5e;

public class ConditionMonitor {
    private int max;
    private int wound;
    private int woundModiferStep;
    private int woundModiferThreshold;

    public void init(int max) {
        this.max = max;
        this.wound = 0;
        this.woundModiferStep = 3;
        this.woundModiferThreshold = 0;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getWound() {
        return wound;
    }

    public void setWound(int wound) {
        this.wound = wound;
    }

    public int getWoundModiferStep() {
        return woundModiferStep;
    }

    public void setWoundModiferStep(int woundModiferStep) {
        this.woundModiferStep = woundModiferStep;
    }

    public int getWoundModiferThreshold() {
        return woundModiferThreshold;
    }

    public void setWoundModiferThreshold(int woundModiferThreshold) {
        this.woundModiferThreshold = woundModiferThreshold;
    }
}
