package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "reset", args = {})
public class ResetDeckCommand extends UndoableDracaGameCommand {

    public ResetDeckCommand(Object[] args) {
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().reset();
        sendMsg("牌库已被重置。");
    }
}
