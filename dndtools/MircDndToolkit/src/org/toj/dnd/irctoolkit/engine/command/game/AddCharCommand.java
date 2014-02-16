package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "addchar", args = { CommandSegment.LIST } )
public class AddCharCommand extends UndoableTopicCommand {

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
