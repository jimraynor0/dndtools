package org.toj.dnd.irctoolkit.engine.command.game.sr5e;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;

@IrcCommand(command = "startbattle", args = {})
public class StartBattleCommand extends Sr5eGameCommand {

    public StartBattleCommand(Object[] args) {}

    @Override
    public void doProcess() {
        getGame().startBattle();
    }
}
