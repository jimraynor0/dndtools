package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(patterns = { "showtopic" }, argsMin = 1, argsMax = 1)
public class ShowTopicCommand extends GameCommand {

    public ShowTopicCommand() {
        super();
    }

    public ShowTopicCommand(String[] args) {
        this();
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        this.sendMsg(getGame().generateTopic());
        return this.msgs;
    }
}
