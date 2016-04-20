package org.toj.dnd.irctoolkit.engine.command.sr5e;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "rename", args = { CommandSegment.STRING,
        CommandSegment.STRING }, summary = ".rename <旧名字> <新名字> - 把名为<旧名字>的PC更名为<新名字>")
public class RenamePcCommand extends Sr5eGameCommand {

    private String oldName;
    private String newName;

    public RenamePcCommand(Object[] args) {
        this.oldName = (String) args[0];
        this.newName = (String) args[1];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().renamePc(oldName, newName);;
        refreshTopic();
    }
}
