package org.toj.dnd.irctoolkit.engine.command.game.common;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "removealias", args = { CommandSegment.STRING })
public class RemoveAliasCommand extends UndoableTopicCommand {
    private String alias;

    public RemoveAliasCommand(Object[] args) {
        this((String) args[0]);
    }

    public RemoveAliasCommand(String alias) {
        this.alias = alias;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().removeAlias(alias);
    }
}