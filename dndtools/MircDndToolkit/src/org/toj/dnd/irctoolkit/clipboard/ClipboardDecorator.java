package org.toj.dnd.irctoolkit.clipboard;

import java.util.ArrayList;
import java.util.List;

import org.toj.dnd.irctoolkit.filter.ViewPoint;
import org.toj.dnd.irctoolkit.io.clipboard.ClipboardAccessor;
import org.toj.dnd.irctoolkit.map.MapGridCell;
import org.toj.dnd.irctoolkit.util.MapGridLoader;

public class ClipboardDecorator {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ClipboardAccessor clipboardAccessor = ClipboardAccessor.getInstance();
        String content = clipboardAccessor.getClipboardContents();
        MapGridCell[][] mapGrid = MapGridLoader.createMapGrid(content);
        // MapGrid map = new MapGrid(mapGrid);

        // 结算光源
        // List<ViewPoint> vps1 = new ArrayList<ViewPoint>();
        // vps1.add(new ViewPoint(4, 7));
        // vps1.add(new ViewPoint(9, 7));
        // vps1.add(new ViewPoint(11, 10));
        // map = new LineOfSightFilter(new MapGrid(mapGrid), vps1,
        // LineOfSightFilter.MASK_TYPE_LINE_OF_SIGHT).applyFilter();

        // 结算视线
        List<ViewPoint> vps2 = new ArrayList<ViewPoint>();
        // vps2.add(new ViewPoint("RG"));
        // vps2.add(new ViewPoint("FT"));
        // vps2.add(new ViewPoint("WL"));
        // vps2.add(new ViewPoint("DR"));
        // vps2.add(new ViewPoint("d3"));
        // map = new LineOfSightFilter(vps2,
        // LineOfSightFilter.MASK_TYPE_LINE_OF_SIGHT).applyFilter(map);

        // 添加坐标
        // map = new AxisLabelFilter().applyFilter(map);

        // 过滤隐形生物
        // List<String> invs = new ArrayList<String>();
        // invs.add("d1");
        // map = new InvisibilityFilter(map, invs).applyFilter();

        // clipboardAccessor.setClipboardContents(MapGridIrcFormatter.format(map));
    }
}
