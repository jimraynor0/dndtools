package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class AddStateCommand extends UndoableTopicCommand {

    private String stateStr;
    private String[] chars;

    public AddStateCommand(String stateStr, String[] chars) {
        super();
        this.stateStr = stateStr;
        this.chars = chars;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            getGame().applyState(caller, stateStr);
        } else {
            for (String charName : chars) {
                getGame().applyState(charName, stateStr);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
