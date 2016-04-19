package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.dnd3r.Dnd3rGame;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "charstat", args = { CommandSegment.NULLABLE_LIST },
        summary = ".charstat <PC名/ALL> - 显示PC的完整状态。可以用ALL做参数显示所有PC的状态(会非常长，强烈建议在小窗中使用)。如果不提供PC名将显示当前昵称的状态。")
public class CharStateCommand extends GameCommand {
    private String[] charNames;

    public CharStateCommand(Object[] args) {
        if (args.length > 0) {
            this.charNames = new String[args.length];
            System.arraycopy(args, 0, this.charNames, 0, args.length);
        }
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        if (charNames == null) {
            for (String line : ((Dnd3rGame) getGame()).getStatString(new String[] { caller }).split("\r\n")) {
                sendMsg(line);
            }
        } else if (charNames.length == 1 && "ALL".equals(charNames[0])) {
            for (String line : ((Dnd3rGame) getGame()).getStatString().split("\r\n")) {
                sendMsg(line);
            }
        } else {
            for (String line : ((Dnd3rGame) getGame()).getStatString(charNames).split("\r\n")) {
                sendMsg(line);
            }
        }
        return this.msgs;
    }
}
