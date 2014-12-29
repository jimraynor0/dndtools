package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "rename", args = { CommandSegment.STRING,
    CommandSegment.STRING })
public class RenameCharCommand extends Dnd3rGameCommand {

    private String oldName;
    private String newName;

    public RenameCharCommand(Object[] args) {
        this.oldName = (String) args[0];
        this.newName = (String) args[1];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().renameChar(oldName, newName);
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
