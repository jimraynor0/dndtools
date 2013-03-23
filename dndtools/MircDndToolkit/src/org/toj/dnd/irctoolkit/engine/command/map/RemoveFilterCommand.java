package org.toj.dnd.irctoolkit.engine.command.map;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class RemoveFilterCommand extends MapCommand {

    private int[] selected;

    public RemoveFilterCommand(int[] selectedRows) {
        this.selected = selectedRows;
    }

    @Override
    protected void doExecute() throws ToolkitCommandException {
        for (int i = selected.length - 1; i >= 0; i--) {
            context.getFilterList().remove(selected[i]);
        }
    }

    @Override
    protected boolean viewChanged() {
        return true;
    }

    @Override
    protected boolean filterChanged() {
        return true;
    }
}
