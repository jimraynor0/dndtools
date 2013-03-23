package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class RemoveStateCommand extends UndoableTopicCommand {

    private String stateStr;
    private String[] chars;

    public RemoveStateCommand(String stateStr, String[] chars) {
        this.stateStr = stateStr;
        this.chars = chars;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            getGame().removeState(caller, stateStr);
        } else {
            for (String charName : chars) {
                getGame().removeState(charName, stateStr);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
