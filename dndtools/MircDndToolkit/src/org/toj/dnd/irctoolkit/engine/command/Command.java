package org.toj.dnd.irctoolkit.engine.command;

import java.util.LinkedList;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.ToolkitContext;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public abstract class Command {
    protected static ToolkitContext context;
    private boolean processResponse = true;
    protected List<OutgoingMsg> msgs = new LinkedList<OutgoingMsg>();

    public static void init(ToolkitContext context) {
        Command.context = context;
    }

    public abstract List<OutgoingMsg> execute() throws ToolkitCommandException;

    public abstract boolean updatesTopic();

    public abstract boolean updatesMap();

    public abstract boolean undoable();

    public abstract void undo() throws ToolkitCommandException;

    protected Game getGame() {
        return context.getGame();
    }

    public boolean isProcessResponse() {
        return processResponse;
    }

    public void setProcessResponse(boolean processResponse) {
        this.processResponse = processResponse;
    }
}
