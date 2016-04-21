package org.toj.dnd.irctoolkit.engine.command.game.sr5e;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "start", args = { CommandSegment.NULLABLE_INT })
public class StartRoundCommand extends Sr5eGameCommand {
    private int startAt;

    public StartRoundCommand(Object[] args) {
        if (args.length == 0) {
            this.startAt = -1;
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
        if (startAt == -1) {
            startAt = getGame().getBattle().getRound();
        }
        getGame().getBattle().startRound(startAt);
        refreshTopic();
        sendMsg("轮到" + getGame().getBattle().getCurrent().getName() + "行动了");
    }
}
