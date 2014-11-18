package org.toj.dnd.irctoolkit.engine.command.ui;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.util.MapGridIrcFormatter;

@IrcCommand(command = "dmmap", args = {})
public class PrintMapForDmCommand extends GameCommand {

    public PrintMapForDmCommand(Object[] args) {
        super();
    }

    @Override
    public List<OutgoingMsg> execute() {
        if (getGame().getDm() == null || !getGame().getDm().equals(caller)) {
            this.msgs.add(new OutgoingMsg(chan, caller, "这个命令只对dm可用，pc要乖乖的喔～",
                    OutgoingMsg.WRITE_TO_MSG, null, -1));
        } else {
            for (String line : MapGridIrcFormatter.formatToList(context
                    .getCurrentMap().getUnfilteredMap())) {
                this.msgs.add(new OutgoingMsg(caller, caller, line,
                        OutgoingMsg.WRITE_TO_MSG, null, -1));
            }
        }
        return this.msgs;
    }
}
