package org.toj.dnd.irctoolkit.engine.command.ui;

import java.util.LinkedList;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.filter.MapFilter;

public class CopyFilterCommand extends MapCommand {

    private int[] srcRows;
    private int destIndex;

    public CopyFilterCommand(int[] srcRows, int destIndex) {
        this.srcRows = srcRows;
        this.destIndex = destIndex;
    }

    @Override
    protected void doExecute() throws ToolkitCommandException {
        LinkedList<MapFilter> toBeAdded = new LinkedList<MapFilter>();
        for (int i : srcRows) {
            toBeAdded.add(context.getFilterList().get(i));
        }
        context.getFilterList().addAll(destIndex, toBeAdded);
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
