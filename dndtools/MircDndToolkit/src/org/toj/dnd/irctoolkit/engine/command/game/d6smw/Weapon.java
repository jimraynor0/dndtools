package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

public class Weapon extends Equipment {
    private Ammo ammo;

    public String fire(TimePoint firingOn) {
        if (ammo.hasAmmo()) {
            return "弹药打光了呢亲……";
        }
        ammo.load();
        return super.activate(firingOn);
    }
}
