package org.toj.dnd.irctoolkit.engine.command.game.sr5e;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.game.sr5e.Combatant;

@IrcCommand(command = "set", args = { CommandSegment.INT,
    CommandSegment.NULLABLE_STRING, CommandSegment.STRING })
public class SetCommand extends Sr5eGameCommand {

    private String name;
    private String attr;
    private int value;

    public SetCommand(Object[] args) {
        this.value = (Integer) args[0];
        this.name = (String) args[1];
        this.attr = (String) args[2];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (name == null) {
            name = caller;
        }
        if ("init".equalsIgnoreCase(attr) && getGame().isInBattle()) {
            getGame().getBattle().setCombatantInit(name, value);
            return;
        }
        Combatant c = getGame().findCharByNameOrAbbre(name);
        if (c != null) {
            if ("cmp".equalsIgnoreCase(attr)) {
                c.getPhysical().init(value);
            }
            if ("cms".equalsIgnoreCase(attr)) {
                c.getStun().init(value);
            }
            if ("woundp".equalsIgnoreCase(attr)) {
                c.getPhysical().setWound(value);
            }
            if ("wounds".equalsIgnoreCase(attr)) {
                c.getStun().setWound(value);
            }
            if ("wmstepp".equalsIgnoreCase(attr)) {
                c.getPhysical().setWoundModiferStep(value);
            }
            if ("wmsteps".equalsIgnoreCase(attr)) {
                c.getStun().setWoundModiferStep(value);
            }
            if ("wmthresholdp".equalsIgnoreCase(attr)) {
                c.getPhysical().setWoundModiferThreshold(value);
            }
            if ("wmthresholds".equalsIgnoreCase(attr)) {
                c.getStun().setWoundModiferThreshold(value);
            }
        }
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
