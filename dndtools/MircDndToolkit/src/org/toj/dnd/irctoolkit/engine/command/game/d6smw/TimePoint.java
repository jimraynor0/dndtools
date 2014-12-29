package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

public class TimePoint {
    public int round;
    public int init;

    public TimePoint(int round, int init) {
        this.round = round;
        this.init = init;
    }

    public boolean before(TimePoint tp) {
        if (round < tp.round) {
            return true;
        }
        if (round > tp.round) {
            return false;
        }
        return init > tp.init;
    }
}
