package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

// temporarily skipped
public class SaveStateCommand extends UndoableTopicCommand {

    private String charName;
    private String stateStr;
    private int save;

    public SaveStateCommand(int save, String stateStr) {
        this.stateStr = stateStr;
        this.save = save;
    }

    public SaveStateCommand(int save, String stateStr, String charName) {
        this.charName = charName;
        this.stateStr = stateStr;
        this.save = save;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (save >= 10) {
            if (charName == null) {
                charName = this.caller;
            }
            getGame().removeState(charName, stateStr);
            sendTopic(getGame().generateTopic());
            if (topicRefreshNeeded) {
                refreshTopic();
            }
        }
    }
}
