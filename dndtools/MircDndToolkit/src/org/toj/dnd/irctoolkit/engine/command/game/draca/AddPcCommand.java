package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "addpc", args = { CommandSegment.NULLABLE_LIST },
        summary = ".addpc <PC名列表> - 将新PC加入游戏。可以一次加入多个PC，PC名用空格分隔。若不提供PC名参数则将当前昵称作为PC名加入游戏。")
public class AddPcCommand extends UndoableDracaGameCommand {

    private String[] charNames;

    public AddPcCommand(Object[] args) {
        if (args != null && args.length > 0) {
            charNames = new String[args.length];
            System.arraycopy(args, 0, charNames, 0, args.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (charNames == null) {
            getGame().addPc(caller);
            sendMsg("新PC加入: " + caller);
        } else {
            for (String ch : charNames) {
                getGame().addPc(ch);
                sendMsg("新PC加入: " + ch);
            }
        }
    }
}
