package org.toj.dnd.irctoolkit.engine.command.game.common;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "actas", args = { CommandSegment.STRING })
public class ActAsCommand extends UndoableTopicCommand {
    private String actAs;

    public ActAsCommand(Object[] args) {
        this.actAs = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (actAs.equals("DM")) {
            getGame().setDm(caller);
            sendMsg("游戏主持人已被设置为：" + caller);
        } else {
            getGame().addAlias(caller, actAs);
        }
    }
}
