package org.toj.dnd.irctoolkit.engine.command.game.draca;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.Zone;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "hand", args = {}, summary = ".hand - 查看自己的手牌。手牌的内容会在小窗中显示。")
public class ShowHandCommand extends DracaGameCommand {

    public ShowHandCommand(Object[] args) {
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException, ToolkitWarningException {
        whisper(caller, "你的手牌: " + getGame().getPcHand(caller).toText());
        if (!getGame().getPcs().get(caller).getZone(Zone.LOCKED).isEmpty()) {
            whisper(caller, "你扣下的牌: " + getGame().getPcs().get(caller).getZone(Zone.LOCKED).toText());
        }
        return msgs;
    }
}
