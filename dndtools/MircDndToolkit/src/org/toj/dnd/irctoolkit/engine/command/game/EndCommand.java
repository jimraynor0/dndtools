package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class EndCommand extends UndoableTopicCommand {

    protected boolean updateTopic = true;

    @Override
    public boolean topicRefreshNeeded() {
        return true;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        List<String> stateMsgs = getGame().getBattle().onTurnEnd();
        for (String msg : stateMsgs) {
            sendMsgToDefaultChan(msg);
        }
        getGame().getBattle().end();
        stateMsgs = getGame().getBattle().onTurnStart();
        for (String msg : stateMsgs) {
            sendMsgToDefaultChan(msg);
        }
        sendTopic(getGame().generateTopic());
        refreshTopic();
        sendMsgToDefaultChan("轮到"
                + getGame().getBattle().getCurrent().getName() + "行动了");
    }
}
