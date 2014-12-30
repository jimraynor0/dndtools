package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;

@IrcCommand(command = "endbattle", args = {})
public class EndBattleCommand extends D6smwGameCommand {

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
