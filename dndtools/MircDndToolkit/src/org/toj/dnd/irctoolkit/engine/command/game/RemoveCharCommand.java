package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command="kill", args = {CommandSegment.LIST})
public class RemoveCharCommand extends UndoableTopicCommand {

    private String[] chars;

    public RemoveCharCommand(Object[] args) {
        this.chars = new String[args.length];
        System.arraycopy(args, 0, this.chars, 0, args.length);
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        for (String charName : chars) {
            getGame().getBattle().removeChar(charName);
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
