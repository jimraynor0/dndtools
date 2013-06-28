package org.toj.dnd.irctoolkit.filter;

import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.map.MapGridCell;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.AxisUtil;

public class AxisLabelFilter extends MapFilter {

    private static final long serialVersionUID = 5226719747025737291L;

    public static final MapGridCell AXIS_LABEL = new MapGridCell(Color.BLACK,
            Color.WHITE, "¨K");

    private transient MapGridCell[][] originalMap;

    private MapGrid mapGrid;

    public AxisLabelFilter(MapGrid mapGrid) {
        this.mapGrid = mapGrid;
    }

    @Override
    public MapGridCell[][] doApplyFilter(MapGridCell[][] map) {
        int xMin = 0;
        int xMax = mapGrid.getWidth() - 1;
        int yMin = 0;
        int yMax = mapGrid.getHeight() - 1;

        for (MapFilter filter : mapGrid.getFilterList()) {
            if (filter instanceof CropFilter) {
                xMax = xMin + ((CropFilter) filter).getxMax();
                xMin += ((CropFilter) filter).getxMin();
                yMax = yMin + ((CropFilter) filter).getyMax();
                yMin += ((CropFilter) filter).getyMin();
            }
        }

        this.originalMap = map;
        MapGridCell[][] maskedGrid = new MapGridCell[originalMap.length + 1][originalMap[0].length + 1];
        maskedGrid[0][0] = AXIS_LABEL;

        Color foreColor = Color.MAROON;
        for (int i = 0; i <= xMax - xMin; i++) {
            foreColor = foreColor == Color.MAROON ? Color.NAVY : Color.MAROON;
            maskedGrid[i + 1][0] = new MapGridCell(foreColor, Color.WHITE,
                    AxisUtil.toString("x", xMin + i));
        }
        foreColor = Color.MAROON;
        for (int i = 0; i <= yMax - yMin; i++) {
            foreColor = foreColor == Color.MAROON ? Color.NAVY : Color.MAROON;
            maskedGrid[0][i + 1] = new MapGridCell(foreColor, Color.WHITE,
                    AxisUtil.toString("y", yMin + i));
        }
        copyOriginalMap(maskedGrid);

        return maskedGrid;
    }

    private void copyOriginalMap(MapGridCell[][] maskedGrid) {
        for (int i = 0; i < originalMap.length; i++) {
            System.arraycopy(originalMap[i], 0, maskedGrid[i + 1], 1,
                    originalMap[0].length);
        }
    }

    @Override
    public String getType() {
        return "×ø±êÂË¾µ";
    }

    @Override
    public String getParams() {
        return "";
    }
}
