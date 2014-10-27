package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "charstat", args = { CommandSegment.NULLABLE_LIST })
public class CharStateCommand extends GameCommand {
    private String[] charNames;

    public CharStateCommand(Object[] args) {
        if (args.length > 0) {
            this.charNames = new String[args.length];
            System.arraycopy(args, 0, this.charNames, 0, args.length);
        }
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        if (charNames == null) {
            for (String line : getGame().getStatString(new String[] { caller })
                    .split("\r\n")) {
                sendMsg(line);
            }
        } else if (charNames.length == 1 && "ALL".equals(charNames[0])) {
            for (String line : getGame().getStatString().split("\r\n")) {
                sendMsg(line);
            }
        } else {
            for (String line : getGame().getStatString(charNames).split("\r\n")) {
                sendMsg(line);
            }
        }
        return this.msgs;
    }
}
