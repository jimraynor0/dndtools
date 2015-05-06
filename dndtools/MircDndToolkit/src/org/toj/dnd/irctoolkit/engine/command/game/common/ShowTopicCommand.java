package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "showtopic", args = {}, summary = ".showtopic - 将频道标题在聊天频道或小窗中显示出来，一般用于大家都没有帽子的情况。")
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
