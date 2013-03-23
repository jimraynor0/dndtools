package org.toj.dnd.irctoolkit.engine.command.map;

import java.util.ArrayList;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.map.MapModel;

public class EraseWithinAreaCommand extends MapCommand {

    private int[] posXs;
    private int[] posYs;
    private List<MapModel> models;

    public EraseWithinAreaCommand(int[] posXs, int[] posYs) {
        this.posXs = posXs;
        this.posYs = posYs;
    }

    public EraseWithinAreaCommand(int[] posXs, int[] posYs,
            List<MapModel> models) {
        this(posXs, posYs);
        this.models = models;
    }

    @Override
    protected void doExecute() throws ToolkitCommandException {
        context.getCurrentMap().eraseObjects(arrayToList(posXs),
                arrayToList(posYs), models);
    }

    private List<Integer> arrayToList(int[] array) {
        List<Integer> result = new ArrayList<Integer>(array.length);
        for (int i : array) {
            result.add(i);
        }
        return result;
    }

    @Override
    protected boolean mapChanged() {
        return true;
    }

    @Override
    protected boolean modelChanged() {
        // TODO Auto-generated method stub
        return false;
    }

}
