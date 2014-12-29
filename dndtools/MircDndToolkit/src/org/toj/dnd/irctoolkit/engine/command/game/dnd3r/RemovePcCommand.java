package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "removepc", args = { CommandSegment.NULLABLE_LIST })
public class RemovePcCommand extends Dnd3rGameCommand {

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
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
