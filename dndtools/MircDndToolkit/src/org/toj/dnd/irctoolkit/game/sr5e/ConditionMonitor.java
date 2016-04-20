package org.toj.dnd.irctoolkit.game.sr5e;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionMonitor {
    private String typeSign;
    private int max;
    private int wound;
    private int woundModiferStep;
    private int woundModiferThreshold;

    public ConditionMonitor(String typeSign) {
        this.typeSign = typeSign;
    }

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

    public boolean isWounded() {
        return this.wound > 0;
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

    public String toStatusString() {
        if (!isWounded()) {
            return "";
        }
        return new StringBuilder("-").append(wound).append(typeSign).toString();
    }
}
