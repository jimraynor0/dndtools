package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(
        command = "+",
        args = { CommandSegment.STRING, CommandSegment.NULLABLE_LIST },
        summary = ".+状态名<|持续轮数> <PC/NPC列表> - 将[状态]加给PC或NPC。可以在状态名后面使用竖线(|)加数字来指定持续轮数。多个PC/NPC名字之间用空格分隔，如果不提供PC/NPC的名字则状态将加给当前昵称。")
public class AddStateCommand extends Dnd3rGameCommand {

    private String stateStr;
    private String[] chars;

    public AddStateCommand(Object[] args) {
        this.stateStr = (String) args[0];
        if (args.length == 1) {
            chars = new String[0];
        } else {
            chars = new String[args.length - 1];
            System.arraycopy(args, 1, chars, 0, chars.length);
        }
    }

    public AddStateCommand(String stateStr, String[] chars) {
        super();
        this.stateStr = stateStr;
        this.chars = chars;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            getGame().applyState(caller, stateStr);
        } else {
            for (String charName : chars) {
                getGame().applyState(charName, stateStr);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
