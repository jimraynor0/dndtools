package org.toj.dnd.irctoolkit.engine.command.map;

import java.util.LinkedList;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.map.MapModel;

public class MoveModelCommand extends MapCommand {

    private int[] srcRows;
    private int destIndex;

    public MoveModelCommand(int[] srcRows, int destIndex) {
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

        for (int i = srcRows.length - 1; i >= 0; i--) {
            srcRows[i] = srcRows[i] < destIndex ? srcRows[i] : srcRows[i]
                    + srcRows.length;
            context.getModelList().remove(srcRows[i]);
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
