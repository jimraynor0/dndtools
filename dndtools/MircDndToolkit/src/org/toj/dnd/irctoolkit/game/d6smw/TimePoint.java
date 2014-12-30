package org.toj.dnd.irctoolkit.game.d6smw;

public class TimePoint {
    public int round;
    public int init;

    public TimePoint(int round, int init) {
        this.round = round;
        this.init = init;
    }

    /**
     * check if this is before tp
     * this is considered before tp if this.round is smaller than tp.round, or if this.round equals tp.round and this.init is greater than tp.init 
     */
    public boolean before(TimePoint tp) {
        if (round < tp.round) {
            return true;
        }
        if (round > tp.round) {
            return false;
        }
        return init > tp.init;
    }

    /**
     * the opposite of before
     */
    public boolean after(TimePoint tp) {
        if (round > tp.round) {
            return true;
        }
        if (round < tp.round) {
            return false;
        }
        return init < tp.init;
    }

    /**
     * the round number from this to tp
     */
    public int getRoundsUntil(TimePoint tp) {
        if (after(tp)) {
            return 0;
        }
        int rounds = tp.round - round;
        if (init > tp.init) {
            rounds++;
        }
        return rounds;
    }
}
