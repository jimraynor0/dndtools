package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "thp", args = { CommandSegment.INT,
    CommandSegment.NULLABLE_LIST })
public class TempHitPointsCommand extends Dnd3rGameCommand {

    private String[] chars;
    private int value;

    public TempHitPointsCommand(Object[] args) {
        value = (Integer) args[0];
        if (args.length > 1) {
            chars = new String[args.length - 1];
            System.arraycopy(args, 1, chars, 0, chars.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars == null) {
            getGame().addThp(caller, value);
        } else {
            for (String ch : chars) {
                getGame().addThp(ch, value);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
