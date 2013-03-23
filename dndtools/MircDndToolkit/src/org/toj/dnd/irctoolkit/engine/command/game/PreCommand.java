package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class PreCommand extends UndoableTopicCommand {

    @Override
    public boolean topicRefreshNeeded() {
        return true;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().getBattle().pre();
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
