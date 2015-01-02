package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.d6smw.Unit;

@IrcCommand(command = "init", args = { CommandSegment.INT,
        CommandSegment.NULLABLE_LIST })
public class InitCommand extends D6smwGameCommand {

    private String[] charName;
    private int init;

    public InitCommand(Object[] args) {
        init = (Integer) args[0];

        if (args.length > 1) {
            charName = new String[args.length - 1];
            System.arraycopy(args, 1, charName, 0, charName.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (!getGame().inBattle()) {
            getGame().startBattle();
        }
        if (charName == null) {
            charName = new String[] { caller };
        }

        for (String ch : charName) {
            if (getGame().getMech(ch) != null) {
                getGame().getBattle().join(getGame().getMech(ch), init);
            } else {
                getGame().getBattle().join(new Unit(ch), init);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}
