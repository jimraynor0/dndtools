package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(patterns = { "removealias" }, argsMin = 2, argsMax = 2)
public class RemoveAliasCommand extends UndoableTopicCommand {
    private String alias;

    public RemoveAliasCommand(String[] args) {
        this(args[1]);
    }

    public RemoveAliasCommand(String alias) {
        this.alias = alias;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().removeAlias(alias);
    }
}
