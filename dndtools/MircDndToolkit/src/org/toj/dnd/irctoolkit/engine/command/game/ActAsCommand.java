package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(patterns = { "actas" }, argsMin = 2, argsMax = 2)
public class ActAsCommand extends UndoableTopicCommand {
    private String actAs;

    public ActAsCommand(String[] args) {
        this(args[1]);
    }

    public ActAsCommand(String actAs) {
        this.actAs = actAs;
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
