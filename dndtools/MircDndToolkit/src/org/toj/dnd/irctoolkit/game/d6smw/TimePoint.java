package org.toj.dnd.irctoolkit.game.d6smw;

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

    public int getRoundsUntil(TimePoint tp) {
        if (!before(tp)) {
            return 0;
        }
        int rounds = tp.round - round;
        if (init > tp.init) {
            rounds++;
        }
        return rounds;
    }
}
