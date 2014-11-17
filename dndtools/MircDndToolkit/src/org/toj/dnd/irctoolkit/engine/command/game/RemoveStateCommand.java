package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "-", args = { CommandSegment.STRING,
        CommandSegment.NULLABLE_LIST })
public class RemoveStateCommand extends UndoableTopicCommand {

    private String stateStr;
    private String[] chars;

    public RemoveStateCommand(Object[] args) {
        this.stateStr = (String) args[0];
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
            getGame().removeState(caller, stateStr);
        } else {
            for (String charName : chars) {
                getGame().removeState(charName, stateStr);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
