package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class RenameCharCommand extends UndoableTopicCommand {

    private String oldName;
    private String newName;

    public RenameCharCommand(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().renameChar(oldName, newName);
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
