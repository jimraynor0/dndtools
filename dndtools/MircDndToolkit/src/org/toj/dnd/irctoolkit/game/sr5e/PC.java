package org.toj.dnd.irctoolkit.game.sr5e;

public class PC extends Combatant {
    private boolean inBattle = false;

    public void startBattle() {
        inBattle = true;
    }

    public void endBattle() {
        inBattle = false;
    }

    public boolean isInBattle() {
        return inBattle;
    }

    public String getStatusForTopic() {
        if (isInBattle()) {
            return super.getStatusForTopic();
        }

        StringBuilder status = new StringBuilder(getName()).append(getPhysical().toStatusString())
                .append(getStun().toStatusString());
        return status.toString();
    }
}
