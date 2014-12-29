package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

public abstract class Equipment {
    private String name;
    private int cd;
    private TimePoint readyOn;
    private int heat;

    private boolean active;

    public String activate(TimePoint activatingOn) {
        if (!active) {
            return name + "已经损坏.";
        }
        if (readyOn.before(activatingOn)) {
            return name + "还在CD中.";
        }
        this.readyOn =
            new TimePoint(activatingOn.round + cd, activatingOn.init);
        return null;
    }

    public boolean isActive() {
        return active;
    }

    public void active() {
        active = true;
    }

    public void deactive() {
        active = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public TimePoint getReadyOn() {
        return readyOn;
    }

    public void setReadyOn(TimePoint readyOn) {
        this.readyOn = readyOn;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }
}
