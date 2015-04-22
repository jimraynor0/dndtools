package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "removepc", args = { CommandSegment.NULLABLE_LIST })
public class RemovePcCommand extends UndoableDracaGameCommand {

    private String[] charNames;

    public RemovePcCommand(Object[] args) {
        if (args != null && args.length > 0) {
            charNames = new String[args.length];
            System.arraycopy(args, 0, charNames, 0, args.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (charNames == null) {
            getGame().removePc(caller);
            sendMsg("PC已被移除: " + caller);
        } else {
            for (String ch : charNames) {
                getGame().removePc(ch);
                sendMsg("PC已被移除: " + ch);
            }
        }
    }
}
