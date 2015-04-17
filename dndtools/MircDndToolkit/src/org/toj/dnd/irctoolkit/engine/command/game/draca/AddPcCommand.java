package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "addpc", args = { CommandSegment.LIST })
public class AddPcCommand extends DracaGameCommand {

    private String[] charNames;

    public AddPcCommand(Object[] args) {
        charNames = new String[args.length];
        System.arraycopy(args, 0, charNames, 0, args.length);
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        for (String ch : charNames) {
            getGame().addPc(ch);
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
