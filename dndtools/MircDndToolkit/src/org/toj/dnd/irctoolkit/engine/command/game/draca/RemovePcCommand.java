package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "removepc", args = { CommandSegment.NULLABLE_LIST },
        summary = ".removepc <PC名列表> - 从游戏中移除指定PC。可以一次移除多个PC，PC名之间用空格分隔。如果不提供PC名参数则默认移除当前昵称。")
public class RemovePcCommand extends UndoableDracaGameCommand {

    private String[] charNames;

    public RemovePcCommand(Object[] args) {
        if (args != null && args.length > 0) {
            charNames = new String[args.length];
            System.arraycopy(args, 0, charNames, 0, args.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (charNames == null) {
            getGame().removePc(caller);
            sendMsg("PC已被移除: " + caller);
        } else {
            for (String ch : charNames) {
                getGame().removePc(ch);
                sendMsg("PC已被移除: " + ch);
            }
        }
    }
}
