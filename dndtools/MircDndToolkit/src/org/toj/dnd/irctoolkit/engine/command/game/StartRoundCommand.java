package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "start", args = { CommandSegment.NULLABLE_INT })
public class StartRoundCommand extends UndoableTopicCommand {
    private int startAt;

    public StartRoundCommand(Object[] args) {
        if (args.length == 0){
            this.startAt = 1;
        } else {
            this.startAt = (Integer) args[0];
        }
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
