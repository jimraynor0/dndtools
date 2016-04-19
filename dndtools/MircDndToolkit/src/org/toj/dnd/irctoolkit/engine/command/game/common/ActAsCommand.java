package org.toj.dnd.irctoolkit.engine.command.game.common;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "actas", args = { CommandSegment.STRING }, summary = ".actas <PC名/DM> - 将当前昵称映射到PC，或将当前昵称设置为DM。",
        desc = "将当前昵称映射到某名PC之后，所有使用当前昵称发出的命令将视为由该PC发出。将当前昵称设置为DM将允许当前昵称使用DM专有的命令。")
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
