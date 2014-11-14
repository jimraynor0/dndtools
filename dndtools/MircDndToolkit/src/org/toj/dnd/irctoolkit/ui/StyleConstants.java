package org.toj.dnd.irctoolkit.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

public class StyleConstants {

    /* ---------------------- global ---------------------- */
    public static final Dimension APP_WINDOW_DIMENSION = new Dimension(800, 600);

    public static final Font ICON_FONT = new Font(Font.MONOSPACED, Font.PLAIN,
            14);

    public static final Dimension ICON_SIZE = new Dimension(22, 22);

    public static final Dimension BUTTON_SIZE_SMALL = new Dimension(50, 30);

    public static final Dimension BUTTON_SIZE_LONG = new Dimension(90, 30);

    public static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);

    /* ---------------------- model pane ---------------------- */
    public static final int MODEL_PANE_WIDTH = 280;

    public static final Dimension MODEL_PANEL_CONTROL_PANEL_SIZE = new Dimension(
            MODEL_PANE_WIDTH, 64);

    public static final int MODEL_LIST_ROW_HEIGHT = 30;
    public static final Dimension MODEL_LIST_HEADER_SIZE = new Dimension(
            MODEL_PANE_WIDTH, MODEL_LIST_ROW_HEIGHT);
    public static final Dimension MODEL_LIST_COLUMN_0_SIZE = new Dimension(44,
            MODEL_LIST_ROW_HEIGHT);
    public static final Dimension MODEL_LIST_COLUMN_1_SIZE = new Dimension(115,
            MODEL_LIST_ROW_HEIGHT);
    public static final Dimension MODEL_LIST_COLUMN_2_SIZE = new Dimension(44,
            MODEL_LIST_ROW_HEIGHT);
    public static final Dimension MODEL_LIST_COLUMN_3_SIZE = new Dimension(56,
            MODEL_LIST_ROW_HEIGHT);

    public static final Dimension EDIT_MODE_DROPDOWN_SIZE = new Dimension(182, 30);

    /* ---------------------- new map dialog ---------------------- */
    public static final Dimension NEW_MAP_DIALOG_SIZE = new Dimension(170, 120);
}
