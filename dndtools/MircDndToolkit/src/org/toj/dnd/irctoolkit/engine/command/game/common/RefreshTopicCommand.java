package org.toj.dnd.irctoolkit.engine.command.game.common;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "refresh", args = {})
public class RefreshTopicCommand extends UndoableTopicCommand {
    protected boolean updateTopic = true;

    public RefreshTopicCommand() {
        super();
    }

    public RefreshTopicCommand(Object[] args) {
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
