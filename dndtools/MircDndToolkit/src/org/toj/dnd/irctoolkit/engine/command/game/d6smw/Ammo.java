package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

public class Ammo {
    public String type;
    public int rounds;

    public boolean hasAmmo() {
        return rounds > 0;
    }

    public void load() {
        rounds--;
    }
}
