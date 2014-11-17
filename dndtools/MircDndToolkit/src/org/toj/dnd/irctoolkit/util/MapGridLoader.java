package org.toj.dnd.irctoolkit.util;

import java.util.ArrayList;
import java.util.List;

import org.toj.dnd.irctoolkit.clipboard.ColorCode;
import org.toj.dnd.irctoolkit.clipboard.ParsedColor;
import org.toj.dnd.irctoolkit.map.MapGridCell;

public class MapGridLoader {

    private static final char STYLE_START = (char) 3;
    private static final char STYLE_END = (char) 15;

    public static MapGridCell[][] createMapGrid(String content) {
        String[] lines = content.split("\n");

        List<List<MapGridCell>> mapGrid = new ArrayList<List<MapGridCell>>(
                lines.length);

        for (String line : lines) {
            List<MapGridCell> mapRow = new ArrayList<MapGridCell>();
            mapGrid.add(mapRow);

            ParsedColor color = new ParsedColor();
            color.setForeground(1);
            color.setBackground(0);
            color.setLength(0);

            String[] parsed = line.split("" + STYLE_START);

            for (int i = 0; i < parsed.length; i++) {
                color = extractColor(parsed[i], color);
                String segment = parsed[i];
                boolean styleEnd = segment.endsWith("" + STYLE_END);
                if (color != null) {
                    segment = parsed[i].substring(color.getLength());
                    while (!segment.isEmpty()
                            && !("" + STYLE_END).equals(segment)) {
                        String cellValue = null;
                        char ch = segment.charAt(0);
                        if (ch < 256) {
                            cellValue = ""
                                    + ch
                                    + (segment.length() > 1 ? segment.charAt(1)
                                            : " ");
                        } else {
                            cellValue = "" + ch;
                        }

                        MapGridCell cell = new MapGridCell(
                                ColorCode.toColor(color.getForeground()),
                                ColorCode.toColor(color.getBackground()),
                                cellValue);
                        if ("■".equals(cell.getCh())) {
                            cell.setBlockLineOfSight(true);
                            cell.setBlockLineOfEffect(true);
                        }
                        mapRow.add(cell);
                        segment = segment.substring(cellValue.length());
                    }

                }
                if (styleEnd) {
                    color.setForeground(1);
                    color.setBackground(0);
                }
            }
        }
        MapGridCell[][] grid = new MapGridCell[mapGrid.get(0).size()][mapGrid
                .size()];
        for (int i = 0; i < mapGrid.get(0).size(); i++) {
            for (int j = 0; j < mapGrid.size(); j++) {
                grid[i][j] = mapGrid.get(j).get(i);
            }
        }
        return grid;
    }

    private static ParsedColor extractColor(String str, ParsedColor prevColor) {
        try {
            ParsedColor color = new ParsedColor();
            if (str == null || str.length() < 2) {
                return prevColor;
            }

            int foregroundCodeLength = 2;
            String foreground = str.substring(0, foregroundCodeLength);
            try {
                int foregroundCode = Integer.parseInt(foreground);
                if (foregroundCode >= ColorCode.getColorCount()) {
                    foregroundCodeLength = 1;
                    color.setForeground(Integer.parseInt(foreground.substring(
                            0, foregroundCodeLength)));
                } else {
                    color.setForeground(foregroundCode);
                }
            } catch (NumberFormatException e) {
                try {
                    foregroundCodeLength = 1;
                    color.setForeground(Integer.parseInt(foreground.substring(
                            0, foregroundCodeLength)));
                } catch (NumberFormatException e1) {
                    return prevColor;
                }
            }
            if (str.length() >= foregroundCodeLength + 3
                    && ',' == str.charAt(foregroundCodeLength)) {
                String background = str.substring(foregroundCodeLength + 1,
                        foregroundCodeLength + 3);
                try {
                    color.setBackground(Integer.parseInt(background));
                    color.setLength(foregroundCodeLength + 3);
                } catch (NumberFormatException e) {
                    // this should not happen
                    e.printStackTrace();
                    return prevColor;
                }
            } else {
                if (prevColor != null) {
                    color.setBackground(prevColor.getBackground());
                }
                color.setLength(foregroundCodeLength);
            }
            return color;
        } catch (StringIndexOutOfBoundsException e) {
            throw e;
        }
    }
}
