package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;

public class EndBattleCommand extends UndoableTopicCommand {

    @Override
    public void doProcess() {
        getGame().endBattle();
        sendTopic(getGame().generateTopic());
        refreshTopic();
    }
}
