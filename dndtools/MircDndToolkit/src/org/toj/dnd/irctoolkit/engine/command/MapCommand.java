package org.toj.dnd.irctoolkit.engine.command;

import java.util.List;

import javax.swing.SwingWorker;

import org.dom4j.Element;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.map.MapGrid;

public abstract class MapCommand extends GameCommand {
    // private Logger log = Logger.getLogger(this.getClass());

    @Override
    public boolean updatesTopic() {
        return false;
    }

    @Override
    public boolean updatesMap() {
        return true;
    }

    @Override
    public boolean undoable() {
        return true;
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Element snapshot = context.getCurrentMap().toXmlElement();
                context.getHistory().saveMapSnapshot(snapshot);
                doExecute();
                return null;
            }

            protected void done() {
                if (mapChanged()) {
                    ToolkitEngine.getEngine().fireMapChangeEvent();
                }
                if (modelChanged()) {
                    ToolkitEngine.getEngine().fireModelChangeEvent();
                }
                if (viewChanged()) {
                    ToolkitEngine.getEngine().fireViewChangeEvent();
                }
                if (filterChanged()) {
                    ToolkitEngine.getEngine().fireFilterChangeEvent();
                }
            }
        }.execute();
        return this.msgs;
    }

    protected abstract void doExecute() throws ToolkitCommandException;

    @Override
    public void undo() throws ToolkitCommandException {
        Element prev = context.getHistory().retrievePreviousMap();
        context.setCurrentMap(new MapGrid(prev));
        if (mapChanged()) {
            ToolkitEngine.getEngine().fireMapChangeEvent();
        }
        if (modelChanged()) {
            ToolkitEngine.getEngine().fireModelChangeEvent();
        }
        if (viewChanged()) {
            ToolkitEngine.getEngine().fireViewChangeEvent();
        }
        if (filterChanged()) {
            ToolkitEngine.getEngine().fireFilterChangeEvent();
        }
    }

    protected boolean mapChanged() {
        return false;
    }

    protected boolean modelChanged() {
        return false;
    }

    protected boolean filterChanged() {
        return false;
    }

    protected boolean viewChanged() {
        return false;
    }

    @Override
    public boolean topicRefreshNeeded() {
        return false;
    }

    @Override
    public boolean requireGameContext() {
        return false;
    }
}
