package org.toj.dnd.irctoolkit.engine.command.game.sr5e;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "+", args = { CommandSegment.INT,
        CommandSegment.NULLABLE_STRING, CommandSegment.NULLABLE_STRING })
public class HealCommand extends Sr5eGameCommand {

    private int value;
    private String chName;
    private String type;

    public HealCommand(Object[] args) {
        value = (Integer) args[0];
        chName = (String) args[1];
        type = (String) args[2];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chName == null) {
            chName = caller;
        }
        if (type == null) {
            type = "s";
        }
        getGame().findCharByNameOrAbbre(chName).heal(value, type);
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
