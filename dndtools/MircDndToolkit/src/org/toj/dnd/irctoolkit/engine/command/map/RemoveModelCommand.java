package org.toj.dnd.irctoolkit.engine.command.map;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.map.MapModel;

public class RemoveModelCommand extends MapCommand {

    private int[] selected;

    public RemoveModelCommand(int[] selectedRows) {
        this.selected = selectedRows;
    }

    @Override
    protected void doExecute() throws ToolkitCommandException {
        for (int i = selected.length - 1; i >= 0; i--) {
            MapModel model = context.getModelList().remove(selected[i]);
            context.getCurrentMap().eraseObjectsForModel(model);
        }
    }

    @Override
    protected boolean mapChanged() {
        return false;
    }

    @Override
    protected boolean modelChanged() {
        return true;
    }
}
