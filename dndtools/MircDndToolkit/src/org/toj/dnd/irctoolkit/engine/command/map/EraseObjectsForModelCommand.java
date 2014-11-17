package org.toj.dnd.irctoolkit.engine.command.map;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.map.MapModel;

public class EraseObjectsForModelCommand extends MapCommand {

    private MapModel model;

    public EraseObjectsForModelCommand(MapModel model) {
        this.model = model;
    }

    @Override
    protected void doExecute() throws ToolkitCommandException {
        context.getCurrentMap().eraseObjectsForModel(model);
    }

    @Override
    protected boolean mapChanged() {
        return true;
    }
}
