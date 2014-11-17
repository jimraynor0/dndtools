package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "actas", args = { CommandSegment.STRING })
public class ActAsCommand extends UndoableTopicCommand {
    private String actAs;

    public ActAsCommand(Object[] args) {
        this.actAs = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (actAs.equals("DM")) {
            getGame().setDm(caller);
        } else {
            getGame().addAlias(caller, actAs);
        }
    }
}
