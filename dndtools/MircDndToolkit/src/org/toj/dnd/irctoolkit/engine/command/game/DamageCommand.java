package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class DamageCommand extends UndoableTopicCommand {

    private int value;
    private String[] chars;

    public DamageCommand(int value, String[] chars) {
        this.value = value;
        this.chars = chars;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            getGame().damage(caller, value);
        } else {
            for (String charName : chars) {
                getGame().damage(charName, value);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
