package org.toj.dnd.irctoolkit.engine.command.map;

import java.io.IOException;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.filter.MapFilter;
import org.toj.dnd.irctoolkit.map.MapModel;
import org.toj.dnd.irctoolkit.map.MapObject;
import org.toj.dnd.irctoolkit.map.Point;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.TrapUtil;

@IrcCommand(command = "randomtraps", args = { CommandSegment.INT,
        CommandSegment.INT })
public class PlaceRandomTrapCommand extends MapCommand {

    private int numberOfTraps;
    private int trapCr;

    public PlaceRandomTrapCommand(Object[] args) {
        numberOfTraps = (Integer) args[0];
        trapCr = (Integer) args[1];
    }

    @Override
    protected void doExecute() throws ToolkitCommandException {
        StringBuilder filterParam = new StringBuilder();

        for (int i = 0; i < numberOfTraps; i++) {
            Point p = findAvailablePosition();
            MapModel model;
            try {
                model = generateTrapModel(i + 1);
                context.getModelList().add(model);
                context.getCurrentMap().drawObjectsOntoGrid(
                        new int[] { p.getX() }, new int[] { p.getY() }, model);
                if (filterParam.length() > 0) {
                    filterParam.append(",");
                }
                filterParam.append(model.getId());
            } catch (IOException e) {
                throw new ToolkitCommandException(e);
            }
        }
        MapFilter filter = MapFilter.MapFilterFactory.createFilter(
                MapFilter.TYPE_INVISIBILITY_FILTER, filterParam.toString());
        filter.setActive(true);
        context.getFilterList().add(filter);
    }

    private MapModel generateTrapModel(int i) throws IOException {
        MapModel model = new MapModel();
        model.setCh("T" + i);
        model.setDesc(getRandomTrapByLevel().name);
        model.setForeground(Color.BLACK);
        return model;
    }

    private TrapUtil.Trap getRandomTrapByLevel() throws IOException {
        return TrapUtil.getRandomTrap(trapCr);
    }

    private Point findAvailablePosition() {
        int x = (int) (Math.random() * context.getCurrentMap().getWidth());
        int y = (int) (Math.random() * context.getCurrentMap().getHeight());
        MapObject[] objsAtPos = context.getCurrentMap().findObjAt(x, x, y, y);
        if (objsAtPos != null && objsAtPos.length > 0) {
            return findAvailablePosition();
        }
        return new Point(x, y);
    }

    @Override
    protected boolean mapChanged() {
        return true;
    }

    @Override
    protected boolean modelChanged() {
        return true;
    }

    @Override
    protected boolean filterChanged() {
        return true;
    }
}
