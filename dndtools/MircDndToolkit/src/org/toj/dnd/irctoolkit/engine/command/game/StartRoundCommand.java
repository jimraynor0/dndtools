package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class StartRoundCommand extends UndoableTopicCommand {
    private int startAt;

    public StartRoundCommand(int startRound) {
        this.startAt = startRound;
    }

    @Override
    public boolean topicRefreshNeeded() {
        return true;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().getBattle().startRound(startAt);
        sendTopic(getGame().generateTopic());
        refreshTopic();
    }
}
