package org.toj.dnd.irctoolkit.engine.command.map;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.util.MapGridIrcFormatter;

@IrcCommand(command="map", args = {})
public class PrintMapCommand extends GameCommand {

    public PrintMapCommand(Object[] args) {
        super();
    }

    @Override
    public List<OutgoingMsg> execute() {
        for (String line : MapGridIrcFormatter.formatToList(context
                .getCurrentMap().getFilteredMap())) {
            this.msgs.add(new OutgoingMsg(chan, caller, line,
                    OutgoingMsg.WRITE_TO_MSG, null, -1));
        }
        return this.msgs;
    }
}
