package org.toj.dnd.irctoolkit.game.encounter;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.toj.dnd.irctoolkit.game.Power;
import org.toj.dnd.irctoolkit.game.encounter.powerrecharger.PowerRecharger;

public class RechargablePower extends Power {
    private List<PowerRecharger> rechargesOn;

    public RechargablePower(String name, int charges, String desc,
            String rechargesOn) {
        super(name, charges, desc);
        createRechargeConditions(rechargesOn);
    }

    public RechargablePower(Element e) {
        super(e);
        String v = e.elementText("rechargesOn");
        createRechargeConditions(v);
    }

    private void createRechargeConditions(String param) {
        String[] conditions = param.split(",");
        this.rechargesOn = new ArrayList<PowerRecharger>(conditions.length);
        for (String cond : conditions) {
            this.rechargesOn.add(PowerRecharger.condition(cond, this));
        }
    }
}
