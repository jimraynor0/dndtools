package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;

@IrcCommand(command = "endbattle", args = {})
public class EndBattleCommand extends Dnd3rGameCommand {

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
