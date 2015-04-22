package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "rename", args = { CommandSegment.STRING, CommandSegment.STRING })
public class RenamePcCommand extends UndoableDracaGameCommand {

    private String oldName;
    private String newName;

    public RenamePcCommand(Object[] args) {
        oldName = (String) args[0];
        newName = (String) args[1];
    }

    @Override
    public void doProcess() throws ToolkitCommandException, ToolkitWarningException {
        getGame().renamePc(oldName, newName);
        sendMsg("PC [" + oldName + "] 已经改名为 [" + newName + "]");
    }
}
