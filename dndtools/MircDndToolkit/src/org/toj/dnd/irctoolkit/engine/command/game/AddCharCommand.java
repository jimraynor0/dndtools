package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class AddCharCommand extends UndoableTopicCommand {

    private String[] chars;

    public AddCharCommand(String[] chars) {
        this.chars = chars;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            getGame().addChar(caller);
        } else {
            for (String charName : chars) {
                getGame().addChar(charName);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
