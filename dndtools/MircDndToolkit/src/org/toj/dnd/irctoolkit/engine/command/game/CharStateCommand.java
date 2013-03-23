package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public class CharStateCommand extends GameCommand {
    private String[] charNames;

    public CharStateCommand() {
        super();
    }

    public CharStateCommand(String[] charNames) {
        this.charNames = charNames;
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
