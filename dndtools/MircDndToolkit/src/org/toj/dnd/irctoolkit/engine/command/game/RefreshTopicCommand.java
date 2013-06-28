package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(patterns = { "refresh" }, argsMin = 1, argsMax = 1)
public class RefreshTopicCommand extends UndoableTopicCommand {
    protected boolean updateTopic = true;

    public RefreshTopicCommand() {
        super();
    }

    public RefreshTopicCommand(String[] args) {
        this();
    }

    @Override
    public boolean topicRefreshNeeded() {
        return true;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        refreshTopic();
    }
}
