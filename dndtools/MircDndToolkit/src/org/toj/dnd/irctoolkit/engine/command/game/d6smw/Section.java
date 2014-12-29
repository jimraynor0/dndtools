package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import java.util.HashMap;
import java.util.Map;

public class Section {
    private String name;
    private int armor;
    private int hp;
    private int currentMaxHp;
    private int maxHp;
    private Map<String, Equipment> equipments =
        new HashMap<String, Equipment>();

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
}
