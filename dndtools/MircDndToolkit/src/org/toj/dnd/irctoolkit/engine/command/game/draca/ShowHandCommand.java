package org.toj.dnd.irctoolkit.engine.command.game.draca;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "hand", args = {})
public class ShowHandCommand extends DracaGameCommand {

    public ShowHandCommand(Object[] args) {
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException, ToolkitWarningException {
        whisper(caller, "你的手牌: " + getGame().getPcHand(caller).toText());
        return msgs;
    }
}
