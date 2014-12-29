package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "showtopic", args = {})
public class ShowTopicCommand extends GameCommand {

    public ShowTopicCommand(Object[] args) {
        super();
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        this.sendMsg(getGame().generateTopic());
        return this.msgs;
    }
}
