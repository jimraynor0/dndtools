package org.toj.dnd.irctoolkit.engine.command.map;

import java.util.LinkedList;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.map.MapModel;

public class CopyModelCommand extends MapCommand {

    private int[] srcRows;
    private int destIndex;

    public CopyModelCommand(int[] srcRows, int destIndex) {
        this.srcRows = srcRows;
        this.destIndex = destIndex;
    }

    @Override
    protected void doExecute() throws ToolkitCommandException {
        LinkedList<MapModel> toBeAdded = new LinkedList<MapModel>();
        for (int i : srcRows) {
            toBeAdded.add(context.getModelList().get(i));
        }
        context.getModelList().addAll(destIndex, toBeAdded);
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
