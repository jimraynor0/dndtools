package org.toj.dnd.irctoolkit.engine.command.game.sr5e;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.sr5e.PC;

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
        PC pc = getGame().findCharByNameOrAbbre(name);
        if (pc != null) {
            if ("init".equalsIgnoreCase(attr)) {
                pc.setInit(value);
            }
            if ("physical".equalsIgnoreCase(attr)) {
                pc.getPhysical().init(value);
            }
            if ("stun".equalsIgnoreCase(attr)) {
                pc.getStun().init(value);
            }
            if ("woundp".equalsIgnoreCase(attr)) {
                pc.getPhysical().setWound(value);
            }
            if ("wounds".equalsIgnoreCase(attr)) {
                pc.getStun().setWound(value);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
