package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "shuffle", args = {})
public class ShuffleDeckCommand extends UndoableDracaGameCommand {

    public ShuffleDeckCommand(Object[] args) {
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().shuffle();
        sendMsg("牌库已被重洗");
    }
}
