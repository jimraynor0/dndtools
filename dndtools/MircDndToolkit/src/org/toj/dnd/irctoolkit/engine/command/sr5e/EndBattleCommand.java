package org.toj.dnd.irctoolkit.engine.command.sr5e;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;

@IrcCommand(command = "endbattle", args = {}, summary = ".endbattle - 结束当前战斗")
public class EndBattleCommand extends Sr5eGameCommand {

    public EndBattleCommand(Object[] args) {
        super();
    }

    @Override
    public void doProcess() {
        getGame().endBattle();
        refreshTopic();
    }
}
