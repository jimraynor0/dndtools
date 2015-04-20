package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "addpc", args = { CommandSegment.NULLABLE_LIST })
public class AddPcCommand extends DracaGameCommand {

    private String[] charNames;

    public AddPcCommand(Object[] args) {
        if (args != null && args.length > 0) {
            charNames = new String[args.length];
            System.arraycopy(args, 0, charNames, 0, args.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (charNames == null) {
            getGame().addPc(caller);
            sendMsg("新PC加入: " + caller);
        } else {
            for (String ch : charNames) {
                getGame().addPc(ch);
                sendMsg("新PC加入: " + ch);
            }
        }
    }
}
