package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "end", args = {})
public class EndCommand extends Dnd3rGameCommand {

    protected boolean updateTopic = true;

    public EndCommand(Object[] args) {
        super();
    }

    @Override
    public boolean topicRefreshNeeded() {
        return true;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().getBattle().end();
        sendTopic(getGame().generateTopic());
        refreshTopic();
        List<String> stateMsgs = getGame().getBattle().getEventResultBuffer();
        for (String msg : stateMsgs) {
            sendMsg(msg);
        }
        sendMsg("轮到" + getGame().getBattle().getCurrent().getName() + "行动了");
    }
}
