package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "-", args = { CommandSegment.INT,
    CommandSegment.NULLABLE_LIST }, summary = "-伤害 <PC/NPC列表> - 对一个或多个参战人员造成[伤害]点伤害。不提供PC/NPC参数则对当前昵称造成伤害。")
public class DamageCommand extends Dnd3rGameCommand {

    private int value;
    private String[] chars;

    public DamageCommand(Object[] args) {
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
            getGame().damage(caller, value);
        } else {
            for (String charName : chars) {
                getGame().damage(charName, value);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
