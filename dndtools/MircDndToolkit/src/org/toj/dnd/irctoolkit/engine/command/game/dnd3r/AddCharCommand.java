package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "addchar", args = { CommandSegment.LIST },
        summary = ".addchar PC/NPC名列表 - 将PC或NPC加入战斗并置于先攻顺序最后面。必须在战斗开始后使用。")
public class AddCharCommand extends Dnd3rGameCommand {

    private String[] chars;

    public AddCharCommand(Object[] args) {
        chars = new String[args.length];
        System.arraycopy(args, 0, chars, 0, args.length);
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            getGame().addChar(caller);
        } else {
            for (String charName : chars) {
                getGame().addChar(charName);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
