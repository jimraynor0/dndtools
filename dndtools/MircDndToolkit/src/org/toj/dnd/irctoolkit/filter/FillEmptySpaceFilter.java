package org.toj.dnd.irctoolkit.filter;

import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.map.MapGridCell;

public class FillEmptySpaceFilter extends MapFilter {
    private static final long serialVersionUID = 378830851064243494L;

    private transient MapGridCell[][] originalMap;

    @Override
    public MapGridCell[][] doApplyFilter(MapGridCell[][] map) {
        this.originalMap = map;
        MapGridCell[][] maskedGrid = new MapGridCell[originalMap.length][originalMap[0].length];
        copyOriginalMap(maskedGrid);
        return maskedGrid;
    }

    private void copyOriginalMap(MapGridCell[][] maskedGrid) {
        for (int i = 0; i < originalMap.length; i++) {
            for (int j = 0; j < originalMap[0].length; j++) {
                if (originalMap[i][j] == null) {
                    maskedGrid[i][j] = MapGrid.WHITESPACE;
                } else {
                    maskedGrid[i][j] = originalMap[i][j];
                    if (isEmpty(originalMap[i][j].getCh())) {
                        maskedGrid[i][j].setCh(MapGrid.WHITESPACE.getCh());
                    }
                    if (originalMap[i][j].getForeground() == null) {
                        maskedGrid[i][j].setForeground(MapGrid.WHITESPACE
                                .getForeground());
                    }
                    if (originalMap[i][j].getBackground() == null) {
                        maskedGrid[i][j].setBackground(MapGrid.WHITESPACE
                                .getBackground());
                    }
                }
            }
        }
    }

    private boolean isEmpty(String ch) {
        return ch == null || ch.isEmpty() || ch.equals(" ");
    }

    @Override
    public String getType() {
        return "";
    }

    @Override
    public String getParams() {
        return "";
    }
}
