package org.toj.dnd.irctoolkit.engine.command.game.sr5e;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "removepc", args = {
        CommandSegment.NULLABLE_LIST }, summary = ".remove <PC名列表> - 从游戏中移除PC。若不提供PC名字列表则以当前昵称作为PC名。PC名列表可以包含多个PC名，中间用空格分隔。")
public class RemovePcCommand extends Sr5eGameCommand {

    private String[] chars;

    public RemovePcCommand(Object[] args) {
        if (args != null && args.length > 0) {
            this.chars = new String[args.length];
            System.arraycopy(args, 0, this.chars, 0, args.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            getGame().removePc(caller);
        } else {
            for (String charName : chars) {
                getGame().removePc(charName);
            }
        }
        refreshTopic();
    }
}
