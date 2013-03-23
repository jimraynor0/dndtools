package org.toj.dnd.irctoolkit.engine.command.map;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.map.MapObject;
import org.toj.dnd.irctoolkit.map.Point;
import org.toj.dnd.irctoolkit.util.AxisUtil;

public class MoveMapObjectCommand extends MapCommand {

    private MapObject[] objs;
    private Point[] dests;

    public MoveMapObjectCommand(String model, String x, String y) {
        MapObject o = context.getCurrentMap().findObjByIconOrDesc(model);
        if (o != null) {
            objs = new MapObject[] { o };
        }
        dests = new Point[] { new Point(AxisUtil.toNumber(x),
                AxisUtil.toNumber(y)) };
    }

    public MoveMapObjectCommand(String x, String y) {
        dests = new Point[] { new Point(AxisUtil.toNumber(x),
                AxisUtil.toNumber(y)) };
    }

    public MoveMapObjectCommand(int srcXmin, int srcXmax, int srcYmin,
            int srcYmax, int destXbase, int destYbase) {
        objs = context.getCurrentMap().findObjAt(srcXmin, srcXmax, srcYmin,
                srcYmax);
        dests = new Point[objs.length];
        for (int i = 0; i < objs.length; i++) {
            dests[i] = new Point(destXbase + objs[i].getPosX() - srcXmin,
                    destYbase + objs[i].getPosY() - srcYmin);
        }
    }

    @Override
    protected void doExecute() throws ToolkitCommandException {
        if (dests.length == 1 && objs == null && this.caller != null) {
            MapObject o = context.getCurrentMap().findObjByIconOrDesc(caller);
            if (o != null) {
                objs = new MapObject[] { o };
            }
        }
        if (objs == null || objs.length != dests.length) {
            return;
        }
        for (int i = 0; i < objs.length; i++) {
            context.getCurrentMap().moveObject(objs[i], dests[i]);
        }
    }

    @Override
    protected boolean mapChanged() {
        return true;
    }

    @Override
    protected boolean modelChanged() {
        return false;
    }
}
