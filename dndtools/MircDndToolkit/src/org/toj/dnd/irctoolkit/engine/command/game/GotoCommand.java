package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "goto", args = { CommandSegment.NULLABLE_INT,
        CommandSegment.STRING })
public class GotoCommand extends UndoableTopicCommand {

    private String charName;
    private int round = Integer.MIN_VALUE;

    public GotoCommand(Object[] args) {
        this.round = args[0] == null ? Integer.MIN_VALUE : (Integer) args[0];
        this.charName = (String) args[1];
    }

    @Override
    public boolean topicRefreshNeeded() {
        return true;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (this.round == Integer.MIN_VALUE) {
            getGame().getBattle().go(charName);
        } else {
            getGame().getBattle().go(charName, round);
        }
        sendTopic(getGame().generateTopic());
        refreshTopic();
        List<String> stateMsgs = getGame().getBattle().getEventResultBuffer();
        for (String msg : stateMsgs) {
            sendMsgToDefaultChan(msg);
        }
        sendMsgToDefaultChan("�ֵ�"
                + getGame().getBattle().getCurrent().getName() + "�ж���");
    }
}
