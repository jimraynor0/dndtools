package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command="after", args = {CommandSegment.NULLABLE_STRING, CommandSegment.STRING})
public class MoveCharAfterCommand extends UndoableTopicCommand {

    private String toBeMoved;
    private String dest;

    public MoveCharAfterCommand(Object[] args) {
        this.toBeMoved = (String) args[0];
        this.dest = (String) args[1];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (this.toBeMoved == null) {
            this.toBeMoved = caller;
        }
        if (getGame().getBattle().getCurrent() != null && getGame().getBattle().getCurrent()
                .equals(getGame().findCharByNameOrAbbre(toBeMoved))) {
            getGame().getBattle().putCharAfter(toBeMoved, dest);
            sendTopic(getGame().generateTopic());
            refreshTopic();
            List<String> stateMsgs = getGame().getBattle().getEventResultBuffer();
            for (String msg : stateMsgs) {
                sendMsgToDefaultChan(msg);
            }
            sendMsgToDefaultChan("轮到"
                    + getGame().getBattle().getCurrent().getName() + "行动了");
        } else {
            getGame().getBattle().putCharAfter(toBeMoved, dest);
            sendTopic(getGame().generateTopic());
            if (topicRefreshNeeded) {
                refreshTopic();
            }
        }
    }
}
