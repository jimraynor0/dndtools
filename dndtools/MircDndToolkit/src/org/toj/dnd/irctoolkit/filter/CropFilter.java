package org.toj.dnd.irctoolkit.filter;

import org.toj.dnd.irctoolkit.map.MapGridCell;
import org.toj.dnd.irctoolkit.util.AxisUtil;

public class CropFilter extends MapFilter {

    private static final long serialVersionUID = 8884009394174799747L;

    private int xMin;
    private int xMax;
    private int yMin;
    private int yMax;
    private transient MapGridCell[][] originalMap;
    private transient MapGridCell[][] maskedGrid;

    public CropFilter(String xMin, String xMax, String yMin, String yMax) {
        this.xMin = AxisUtil.toNumber(xMin);
        this.xMax = AxisUtil.toNumber(xMax);
        this.yMin = AxisUtil.toNumber(yMin);
        this.yMax = AxisUtil.toNumber(yMax);
    }

    public CropFilter(int xMin, int xMax, int yMin, int yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    @Override
    public synchronized MapGridCell[][] doApplyFilter(MapGridCell[][] original) {
        this.originalMap = original;
        int xBase = 0;
        int yBase = 0;
        maskedGrid = new MapGridCell[xMax - xMin + 1][yMax - yMin + 1];
        copyMap(xBase, yBase);

        return maskedGrid;
    }

    private void copyMap(int xBase, int yBase) {
        for (int i = xMin; i <= xMax; i++) {
            System.arraycopy(originalMap[i], yMin, maskedGrid[xBase], yBase,
                    yMax - yMin + 1);
            xBase++;
        }
    }

    private void copyXAxisLabels() {
        int mappedX = 1;
        for (int i = xMin; i <= xMax; i++) {
            maskedGrid[mappedX][0] = originalMap[i][0];
            mappedX++;
        }
    }

    private void copyYAxisLabels() {
        int mappedY = 1;
        for (int i = yMin; i <= yMax; i++) {
            maskedGrid[0][mappedY] = originalMap[0][i];
            mappedY++;
        }
    }

    private boolean hasAxis() {
        return originalMap[0][0] == AxisLabelFilter.AXIS_LABEL;
    }

    @Override
    public String getType() {
        return "截取滤镜";
    }

    @Override
    public String getParams() {
        return new StringBuilder().append(xMin).append(",").append(xMax)
                .append(",").append(yMin).append(",").append(yMax).toString();
    }

    public int getxMin() {
        return xMin;
    }

    public int getxMax() {
        return xMax;
    }

    public int getyMin() {
        return yMin;
    }

    public int getyMax() {
        return yMax;
    }
}
