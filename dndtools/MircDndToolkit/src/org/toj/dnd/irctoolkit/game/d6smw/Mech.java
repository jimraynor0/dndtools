package org.toj.dnd.irctoolkit.game.d6smw;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;

//@XmlType
//@XmlAccessorType(XmlAccessType.FIELD)
public class Mech extends Unit {
    private int heat;
    private int heatSink;
    private int maxHeat;
    private Map<String, Section> sections = new HashMap<String, Section>();
    private Map<String, Equipment> equipments = new HashMap<String, Equipment>();
    private Map<String, Ammo> ammoTrackers = new HashMap<String, Ammo>();

    public String activateEquipment(String equipment, TimePoint activatingOn) {
        if (!equipments.containsKey(equipment)) {
            return equipment + "不存在。";
        }
        if (isOverHeated()) {
            return "无法启动" + equipment + "，机甲过热。";
        }
        String activateResult = equipments.get(equipment)
                .activate(activatingOn);
        if (activateResult == null) {
            heat += equipments.get(equipment).getHeat();
        }
        return activateResult;
    }

    public void damage(int dmg, String section) {
        sections.get(section).damage(dmg);
    }

    public void quickRepair(int repair, String section) {
        sections.get(section).quickRepair(repair);
    }

    public void completeRepair() {
        for (Section sec : sections.values()) {
            sec.completeRepair();
        }
    }

    public void repairEquipment(String equipment) {
        equipments.get(equipment).active();
    }

    public boolean isOverHeated() {
        return heat > maxHeat;
    }

    public void sinkHeat(int times) {
        heat -= heatSink * times;
        if (heat < 0) {
            heat = 0;
        }
    }

    public boolean hasHeat() {
        return true;
    }

    public void endBattle() {
        setInit(0);
        setSpeed(0);
        setDirection(null);
        heat = 0;
        for (Equipment eq : equipments.values()) {
            eq.setReadyOn(null);
        }
    }

    public List<String> toFullStatString(TimePoint current) {
        List<String> stat = new LinkedList<String>();
        StringBuilder sb = new StringBuilder("驾驶员: ").append(getName());
        sb.append(", 机甲型号: ").append(getModel());
        if (heat > 0) {
            if (heat > maxHeat) {
                sb.append(", ").append(
                        IrcColoringUtil.paint("热量: " + heat + "/" + maxHeat
                                + "(过热)", Color.RED.getCode()));
            } else {
                sb.append(", 热量: ").append(heat).append("/").append(maxHeat);
            }
        }
        if (getDirection() != null) {
            sb.append(", 速度/方向: ").append(getSpeed()).append("/")
                    .append(getDirection());
        }
        stat.add(sb.toString());

        for (String sec : Section.SECTIONS) {
            stat.add(sections.get(sec).toFullStatString(current));
        }

        if (ammoTrackers != null && !ammoTrackers.isEmpty()) {
            sb = new StringBuilder();
            for (Ammo ammo : ammoTrackers.values()) {
                if (sb.length() == 0) {
                    sb.append("弹药: ");
                } else {
                    sb.append(", ");
                }
                if (ammo.hasAmmo()) {
                    sb.append(ammo.getType()).append("(")
                            .append(ammo.getRounds()).append(")");
                } else {
                    sb.append(IrcColoringUtil.paint(ammo.getType() + "("
                            + +ammo.getRounds() + ")", Color.RED.getCode()));
                }
            }
            stat.add(sb.toString());
        }
        stat.add("");
        return stat;
    }

    public String getHpExpression() {
        StringBuilder sb = new StringBuilder();
        for (String sec : Section.SECTIONS) {
            if (sb.length() == 0) {
                sb.append("(");
            } else {
                sb.append("/");
            }
            sb.append(sections.get(sec).getHp());
        }
        return sb.append(")").toString();
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getHeatSink() {
        return heatSink;
    }

    public void setHeatSink(int heatSink) {
        this.heatSink = heatSink;
    }

    public int getMaxHeat() {
        return maxHeat;
    }

    public void setMaxHeat(int maxHeat) {
        this.maxHeat = maxHeat;
    }

    public Map<String, Section> getSections() {
        return sections;
    }

    public void setSections(Map<String, Section> sections) {
        this.sections = sections;
    }

    public Map<String, Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(Map<String, Equipment> equipments) {
        this.equipments = equipments;
    }

    public Map<String, Ammo> getAmmoTrackers() {
        return ammoTrackers;
    }

    public void setAmmoTrackers(Map<String, Ammo> ammoTrackers) {
        this.ammoTrackers = ammoTrackers;
    }
}
