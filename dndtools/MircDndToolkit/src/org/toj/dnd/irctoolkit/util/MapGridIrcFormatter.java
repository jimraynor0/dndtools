package org.toj.dnd.irctoolkit.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.map.MapGridCell;
import org.toj.dnd.irctoolkit.token.Color;

public class MapGridIrcFormatter {

    private static final Logger log = Logger
            .getLogger(MapGridIrcFormatter.class);

    private static final String STYLE_START = "" + (char) 3;
    private static final String STYLE_END = "" + (char) 15;

    public static String format(MapGrid mapGrid) {
        String text = "";
        MapGridCell[][] grid = mapGrid.getGrid();
        for (int j = 0; j < mapGrid.getHeight(); j++) {
            Color foreground = null;
            Color background = null;
            for (int i = 0; i < mapGrid.getWidth(); i++) {
                text += formatCell(grid[i][j], foreground, background);
                foreground = grid[i][j].getForeground();
                background = grid[i][j].getBackground();
            }
            text += "\r\n";
        }
        return text;
    }

    public static List<String> formatToList(MapGrid mapGrid) {
        List<String> result = new LinkedList<String>();
        MapGridCell[][] grid = mapGrid.getGrid();
        for (int j = 0; j < mapGrid.getHeight(); j++) {
            StringBuilder logItem = new StringBuilder();

            StringBuilder line = new StringBuilder();
            Color foreground = null;
            Color background = null;
            for (int i = 0; i < mapGrid.getWidth(); i++) {
                logItem.append(grid[i][j]);

                line.append(formatCell(grid[i][j], foreground, background));
                foreground = grid[i][j].getForeground();
                background = grid[i][j].getBackground();
            }
            log.debug(logItem);
            log.debug(line);
            result.add(line.toString());
        }
        return result;
    }

    private static String formatCell(MapGridCell cell, Color foreground,
            Color background) {
        String ch = cell.getCh() == null ? MapGrid.WHITESPACE.getCh() : cell
                .getCh();
        if (cell.getForeground() == foreground
                && (cell.getBackground() == background || ((cell
                        .getBackground() == null || cell.getBackground() == Color.WHITE) && background == null))) {
            return ch;
        }
        // if (cell.getBackground() == background) {
        // return STYLE_START + cell.getForeground().getCode() + ch;
        // }
        // if ((cell.getBackground() == null || cell.getBackground() ==
        // Color.WHITE)
        // && background != null) {
        // return STYLE_END + STYLE_START + cell.getForeground().getCode()
        // + ch;
        // }
        // return STYLE_START + cell.getForeground().getCode() + ","
        // + cell.getBackground().getCode() + ch;
        if (cell.getBackground() == background
                || ((cell.getBackground() == null || cell.getBackground() == Color.WHITE) && background == null)) {
            return STYLE_START + cell.getForeground().getCode() + ch;
        }
        if ((cell.getBackground() == null || cell.getBackground() == Color.WHITE)
                && background != null) {
            return STYLE_END + STYLE_START + cell.getForeground().getCode()
                    + ch;
        }
        return STYLE_START + cell.getForeground().getCode() + ","
                + cell.getBackground().getCode() + ch;
    }
}
