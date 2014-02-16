package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "ap", args = { CommandSegment.NULLABLE_INT, CommandSegment.NULLABLE_STRING } )
public class ActionPointsCommand extends UndoableTopicCommand {
    private String charName;
    private int usage = 1;

    public ActionPointsCommand(Object[] args) {
        this.usage = args[0] == null ? 1 : (Integer) args[0];
        this.charName = (String) args[1];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (this.charName == null) {
            this.charName = caller;
        }
        getGame().findCharByNameOrAbbre(charName).recordAp(usage);
    }
}
