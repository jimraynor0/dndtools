package org.toj.dnd.irctoolkit.engine.command.game.sr5e;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

import java.util.Arrays;

@IrcCommand(command = "kill", args = {
        CommandSegment.NULLABLE_LIST }, summary = ".kill <角色名列表> - 从战斗中移除角色。若不提供角色名字列表则以当前昵称作为角色名。角色名列表可以包含多个角色名，中间用空格分隔。")
public class KillCommand extends Sr5eGameCommand {

    private String[] chars;

    public KillCommand(Object[] args) {
        if (args != null && args.length > 0) {
            this.chars = new String[args.length];
            System.arraycopy(args, 0, this.chars, 0, args.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (!getGame().isInBattle()) {
            return;
        }

        if (chars.length == 0) {
            getGame().getBattle().remove(caller);
        } else {
            Arrays.stream(chars).forEach(c -> getGame().getBattle().remove(c));
        }
        refreshTopic();
    }
}
