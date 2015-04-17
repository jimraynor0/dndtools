package org.toj.dnd.irctoolkit.engine.command;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.file.GameStore;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public abstract class UndoableTopicCommand extends GameCommand {
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        Object snapshot = GameStore.getSnapshot(getGame());
        context.getHistory().saveGameSnapshot(snapshot);
        doProcess();
        try {
            GameStore.save(getGame());
        } catch (IOException e) {
            log.error("saving game failed", e);
        } catch (JAXBException e) {
            log.error("saving game failed", e);
        }
        return this.msgs;
    }

    @Override
    public boolean undoable() {
        return true;
    }

    @Override
    public void undo() throws ToolkitCommandException {
        Object last = context.getHistory().retrievePreviousGame();
        context.setGame(GameStore.loadSnapshot(last));
    }

    protected abstract void doProcess() throws ToolkitCommandException;
}
