package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;

@IrcCommand(command = "endbattle", args = {})
public class EndBattleCommand extends UndoableTopicCommand {
    
    public EndBattleCommand(Object[] args) {
        super();
    }

    @Override
    public void doProcess() {
        getGame().endBattle();
        sendTopic(getGame().generateTopic());
        refreshTopic();
    }
}
