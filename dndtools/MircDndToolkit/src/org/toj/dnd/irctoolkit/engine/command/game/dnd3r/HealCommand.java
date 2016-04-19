package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "+", args = { CommandSegment.INT, CommandSegment.NULLABLE_LIST },
        summary = "+治疗量 <PC/NPC列表> - 为PC/NPC恢复指定数量的HP。多个受治疗的人员用空格分隔。不指明受治疗的人员则默认治疗当前昵称。")
public class HealCommand extends Dnd3rGameCommand {

    private int value;
    private String[] chars;

    public HealCommand(Object[] args) {
        value = (Integer) args[0];
        if (args.length == 1) {
            chars = new String[0];
        } else {
            chars = new String[args.length - 1];
            System.arraycopy(args, 1, chars, 0, chars.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            getGame().heal(caller, value);
        } else {
            for (String charName : chars) {
                getGame().heal(charName, value);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
