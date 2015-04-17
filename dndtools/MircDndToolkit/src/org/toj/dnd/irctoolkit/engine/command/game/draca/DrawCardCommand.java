package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "draw", args = { CommandSegment.NULLABLE_INT })
public class DrawCardCommand extends DracaGameCommand {

    private int amount = 1;

    public DrawCardCommand(Object[] args) {
        if (args.length > 0) {
            amount = (Integer) args[0];
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        sendMsg(getGame().draw(caller, amount));
    }
}
