package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;

@IrcCommand(command = "rest", args = {})
public class RestCommand extends UndoableTopicCommand {

    public RestCommand(Object[] args) {
        super();
    }

    @Override
    public void doProcess() {
        getGame().applyExtendedRest();
    }
}
