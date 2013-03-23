package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class RemoveCharCommand extends UndoableTopicCommand {

    private String[] chars;

    public RemoveCharCommand(String[] chars) {
        this.chars = chars;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        for (String charName : chars) {
            getGame().getBattle().removeChar(charName);
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
