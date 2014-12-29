package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "pp", args = { CommandSegment.NULLABLE_INT,
    CommandSegment.NULLABLE_STRING })
public class PsionicPointCommand extends Dnd3rGameCommand {
    private String charName;
    private int usage = 1;

    public PsionicPointCommand(Object[] args) {
        if (args[0] != null) {
            usage = (Integer) args[0];
        }
        charName = (String) args[1];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (this.charName == null) {
            this.charName = caller;
        }
        getGame().findCharByNameOrAbbre(charName).recordPp(usage);
    }
}
