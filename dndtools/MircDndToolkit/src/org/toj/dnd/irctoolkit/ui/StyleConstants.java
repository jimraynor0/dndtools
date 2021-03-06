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

    public static final int INPUT_HEIGHT = 25;

    public static final Dimension BUTTON_SIZE_SMALL = new Dimension(50,
            INPUT_HEIGHT);

    public static final Dimension BUTTON_SIZE_LONG = new Dimension(90, 30);

    public static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);

    public static final int DIALOG_HORIZONTAL_MARGIN = 16;
    public static final int DIALOG_VERTICAL_MARGIN = 10;

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

    public static final Dimension EDIT_MODE_DROPDOWN_SIZE = new Dimension(182,
            MODEL_LIST_ROW_HEIGHT);

    /* ---------------------- model editor ---------------------- */
    public static final Dimension MODEL_EDITOR_SIZE = new Dimension(318, 189);

    public static final Dimension ICON_ID_LABEL_SIZE = new Dimension(59,
            INPUT_HEIGHT);
    public static final Dimension ICON_ID_INPUT_SIZE = new Dimension(230,
            INPUT_HEIGHT);

    public static final Dimension ICON_LABEL_SIZE = new Dimension(29,
            INPUT_HEIGHT);
    public static final Dimension ICON_INPUT_SIZE = new Dimension(INPUT_HEIGHT,
            INPUT_HEIGHT);
    public static final Dimension DESC_LABEL_SIZE = new Dimension(61,
            INPUT_HEIGHT);
    public static final Dimension DESC_INPUT_SIZE = new Dimension(162,
            INPUT_HEIGHT);

    public static final Dimension COLOR_DEPTH_RADIO_BUTTON_SIZE = new Dimension(
            89, INPUT_HEIGHT);
    public static final Dimension COLOR_DROPDOWN_SIZE = new Dimension(103,
            INPUT_HEIGHT);

    public static final Dimension BLOCK_LOE_CHECKBOX_SIZE = new Dimension(124,
            INPUT_HEIGHT);

    /* ---------------------- new map dialog ---------------------- */
    public static final Dimension NEW_MAP_DIALOG_SIZE = new Dimension(180, 120);
    public static final Dimension NEW_MAP_INPUT_SIZE = new Dimension(120,
            INPUT_HEIGHT);

    /* ---------------------- filter editor ---------------------- */
    public static final Dimension CROP_FILTER_PARAM_SIZE = new Dimension(500,
            600);
    public static final Dimension INV_FILTER_PARAM_SIZE = new Dimension(400,
            550);
    public static final Dimension TEXT_FIELD_PARAM_SIZE = new Dimension(400,
            140);

    public static final Dimension PARAM_TITLE_LABEL_SIZE = new Dimension(400,
            INPUT_HEIGHT);
    public static final Dimension TEXT_PARAM_LABEL_SIZE = new Dimension(40,
            INPUT_HEIGHT);
    public static final Dimension TEXT_PARAM_INPUT_SIZE = new Dimension(298,
            INPUT_HEIGHT);

    /* ---------------------- console pane ---------------------- */
    public static final Dimension CONSOLE_DIALOG_SIZE = new Dimension(400, 445);
    public static final Dimension CONSOLE_LOG_SIZE = new Dimension(400, 400);
    public static final Dimension CONSOLE_INPUT_SIZE = new Dimension(400,
            INPUT_HEIGHT);
}
