package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "addpc", args = { CommandSegment.LIST })
public class AddPcCommand extends UndoableTopicCommand {

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
