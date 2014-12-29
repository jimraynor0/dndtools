package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "start", args = { CommandSegment.NULLABLE_INT })
public class StartRoundCommand extends Dnd3rGameCommand {
    private int startAt;

    public StartRoundCommand(Object[] args) {
        if (args.length == 0) {
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
        List<String> stateMsgs = getGame().getBattle().getEventResultBuffer();
        for (String msg : stateMsgs) {
            sendMsg(msg);
        }
        sendMsg("轮到" + getGame().getBattle().getCurrent().getName() + "行动了");
    }
}
