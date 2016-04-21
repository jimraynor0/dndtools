package org.toj.dnd.irctoolkit.engine.command.game.sr5e;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "addpc", args = { CommandSegment.LIST },
        summary = ".addpc <PC名列表> - 在游戏中创建新PC。若不提供PC名字列表则以当前昵称作为PC名。PC名列表可以包含多个PC名，中间用空格分隔。")
public class AddPcCommand extends Sr5eGameCommand {

    private String[] chars;

    public AddPcCommand(Object[] args) {
        chars = new String[args.length];
        System.arraycopy(args, 0, chars, 0, args.length);
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            getGame().addPc(caller);
        } else {
            for (String charName : chars) {
                getGame().addPc(charName);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
