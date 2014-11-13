package org.toj.dnd.irctoolkit.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

public class StyleConstants {

    /* ---------------------- global ---------------------- */
    public static final Dimension APP_WINDOW_DIMENSION = new Dimension(800, 600);

    public static final Font ICON_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 14);

    public static final int MODEL_PANE_WIDTH = 280;
    public static final Dimension SIZE_ICON_LABEL = new Dimension(22, 22);

    public static final Dimension SIZE_BUTTON = new Dimension(90, 30);
    public static final Dimension SIZE_DROPDOWN = new Dimension(182, 30);

    public static final Insets INSETS_BUTTON = new Insets(0, 0, 0, 0);

    /* ---------------------- model pane ---------------------- */
    public static final Dimension SIZE_CONTROL_PANEL = new Dimension(
            MODEL_PANE_WIDTH, 64);

    public static final int SIZE_MODEL_LIST_ROW_HEIGHT = 30;
    public static final Dimension SIZE_MODEL_LIST_HEADER = new Dimension(
            MODEL_PANE_WIDTH, SIZE_MODEL_LIST_ROW_HEIGHT);
    public static final Dimension SIZE_MODEL_LIST_COLUMN_0 = new Dimension(44,
            SIZE_MODEL_LIST_ROW_HEIGHT);
    public static final Dimension SIZE_MODEL_LIST_COLUMN_1 = new Dimension(
            115, SIZE_MODEL_LIST_ROW_HEIGHT);
    public static final Dimension SIZE_MODEL_LIST_COLUMN_2 = new Dimension(44,
            SIZE_MODEL_LIST_ROW_HEIGHT);
    public static final Dimension SIZE_MODEL_LIST_COLUMN_3 = new Dimension(56,
            SIZE_MODEL_LIST_ROW_HEIGHT);
}
