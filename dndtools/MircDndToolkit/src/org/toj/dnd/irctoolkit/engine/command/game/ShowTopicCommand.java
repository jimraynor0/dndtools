package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public class ShowTopicCommand extends GameCommand {

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        this.sendMsg(getGame().generateTopic());
        return this.msgs;
    }
}
