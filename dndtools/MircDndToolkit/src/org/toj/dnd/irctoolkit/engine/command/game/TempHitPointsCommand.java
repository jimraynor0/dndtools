package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class TempHitPointsCommand extends UndoableTopicCommand {

    private String[] chars;
    private int value;

    public TempHitPointsCommand(int value, String[] chars) {
        this.chars = chars;
        this.value = value;
    }

    public TempHitPointsCommand(int value) {
        this.value = value;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (getGame().inBattle()) {
            if (chars == null) {
                getGame().getBattle().addThp(caller, value);
            } else {
                for (String ch : chars) {
                    getGame().getBattle().addThp(ch, value);
                }
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
