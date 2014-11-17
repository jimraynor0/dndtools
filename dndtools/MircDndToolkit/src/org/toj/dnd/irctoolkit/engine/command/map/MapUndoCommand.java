package org.toj.dnd.irctoolkit.engine.command.map;

import java.util.EmptyStackException;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public class MapUndoCommand extends GameCommand {

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        try {
            context.getHistory().undoUiCmd();
            return this.msgs;
        } catch (EmptyStackException e) {
            throw new ToolkitCommandException("No history record available.");
        }
    }
}
