package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class MoveCharAfterCommand extends UndoableTopicCommand {

    private String toBeMoved;
    private String dest;

    public MoveCharAfterCommand(String toBeMoved, String dest) {
        this.toBeMoved = toBeMoved;
        this.dest = dest;
    }

    public MoveCharAfterCommand(String dest) {
        this.dest = dest;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (this.toBeMoved == null) {
            this.toBeMoved = caller;
        }
        if (getGame().getBattle().getCurrent()
                .equals(getGame().findCharByNameOrAbbre(toBeMoved))) {
            List<String> stateMsgs = getGame().getBattle().onTurnEnd();
            for (String msg : stateMsgs) {
                sendMsgToDefaultChan(msg);
            }
            getGame().getBattle().putCharAfter(toBeMoved, dest);
            stateMsgs = getGame().getBattle().onTurnStart();
            for (String msg : stateMsgs) {
                sendMsgToDefaultChan(msg);
            }
            sendTopic(getGame().generateTopic());
            refreshTopic();
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
