package org.toj.dnd.irctoolkit.engine.command.ui;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class LoadFromClipboardCommand extends MapCommand {

    @Override
    protected boolean mapChanged() {
        return true;
    }

    @Override
    protected boolean modelChanged() {
        return true;
    }

    @Override
    public void doExecute() throws ToolkitCommandException {
        // frame.initMapPanel(new MapGrid(MapGridLoader
        // .createMapGrid(ClipboardAccessor.getInstance()
        // .getClipboardContents())));
        throw new ToolkitCommandException("Not Implemented");
    }
}
