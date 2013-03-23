package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class GotoCommand extends UndoableTopicCommand {

    private String charName;
    private int round = Integer.MIN_VALUE;

    public GotoCommand(String charName, int round) {
        this.charName = charName;
        this.round = round;
    }

    public GotoCommand(String charName) {
        this.charName = charName;
    }

    @Override
    public boolean topicRefreshNeeded() {
        return true;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (this.round == Integer.MIN_VALUE) {
            getGame().getBattle().go(charName);
        } else {
            getGame().getBattle().go(charName, round);
        }
        sendTopic(getGame().generateTopic());
        refreshTopic();
    }
}
