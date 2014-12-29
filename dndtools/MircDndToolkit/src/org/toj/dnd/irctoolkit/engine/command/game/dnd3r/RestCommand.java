package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;

@IrcCommand(command = "rest", args = {})
public class RestCommand extends Dnd3rGameCommand {

    public RestCommand(Object[] args) {
        super();
    }

    @Override
    public void doProcess() {
        getGame().applyExtendedRest();
    }
}
