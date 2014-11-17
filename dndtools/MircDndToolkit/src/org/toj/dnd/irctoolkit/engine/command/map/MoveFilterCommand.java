package org.toj.dnd.irctoolkit.engine.command.map;

import java.util.LinkedList;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.filter.MapFilter;

public class MoveFilterCommand extends MapCommand {

    private int[] srcRows;
    private int destIndex;

    public MoveFilterCommand(int[] srcRows, int destIndex) {
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

        for (int i = srcRows.length - 1; i >= 0; i--) {
            srcRows[i] = srcRows[i] < destIndex ? srcRows[i] : srcRows[i]
                    + srcRows.length;
            context.getFilterList().remove(srcRows[i]);
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
