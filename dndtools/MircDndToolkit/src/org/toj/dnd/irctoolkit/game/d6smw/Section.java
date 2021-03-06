package org.toj.dnd.irctoolkit.game.d6smw;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Section {
    public static final List<String> SECTIONS = Arrays.asList("胸腹", "左臂", "右臂",
            "腿");

    private String name;
    private int armor;
    private int hp;
    private int currentMaxHp;
    private int maxHp;
    private Map<String, Equipment> equipments = new HashMap<String, Equipment>();

    public void damage(int dmg) {
        dmg -= armor;
        if (dmg <= 0) {
            return;
        }
        hp -= dmg;
        if (hp <= 0) {
            hp = 0;
            deactiveEquipments();
        }
    }

    private void deactiveEquipments() {
        for (Equipment e : equipments.values()) {
            e.deactive();
        }
    }

    public void quickRepair(int repair) {
        hp += repair;
        if (hp > currentMaxHp) {
            hp = currentMaxHp;
        }
        currentMaxHp = hp;
    }

    public void completeRepair() {
        hp = maxHp;
        currentMaxHp = maxHp;
    }

    public String toFullStatString(TimePoint current) {
        StringBuilder sb = new StringBuilder(name);
        sb.append("(").append(hp).append("/").append(currentMaxHp).append("/")
                .append(maxHp).append(")");
        if (hp == 0) {
            sb = new StringBuilder(IrcColoringUtil.paint(sb.toString(),
                    Color.RED.getCode()));
        }
        if (equipments != null && !equipments.isEmpty()) {
            sb.append(" - ");
            boolean first = true;
            for (Equipment eq : equipments.values()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(eq.toStatString(current));
            }
        }
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getCurrentMaxHp() {
        return currentMaxHp;
    }

    public void setCurrentMaxHp(int currentMaxHp) {
        this.currentMaxHp = currentMaxHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public Map<String, Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(Map<String, Equipment> equipments) {
        this.equipments = equipments;
    }
}
