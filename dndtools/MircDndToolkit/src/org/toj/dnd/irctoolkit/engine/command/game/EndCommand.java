package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "end", args = {})
public class EndCommand extends UndoableTopicCommand {

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
        sendMsgToDefaultChan("�ֵ�"
                + getGame().getBattle().getCurrent().getName() + "�ж���");
    }
}
