package org.toj.dnd.irctoolkit.ui.map.mappane;

import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.map.MapGridCell;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.ui.StyleConstants;

public class MapCellRenderer extends JLabel implements TableCellRenderer {
    private static final long serialVersionUID = -8140764076998674882L;

    private Logger log = Logger.getLogger(this.getClass());

    private Map<Integer, MatteBorder> borderCache = new HashMap<Integer, MatteBorder>();

    private static final MapGridCell EMPTY_CELL = new MapGridCell(Color.BLACK,
            Color.WHITE, "¡¡");

    public MapCellRenderer() {
        super();
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setFont(StyleConstants.ICON_FONT);
        this.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        MapGridCell cell = value == null ? EMPTY_CELL : (MapGridCell) value;
        this.setText(cell.getCh());
        if (null != cell.getForeground()) {
            this.setForeground(cell.getForeground().getColor());
        } else if (cell.getCh() != null && !cell.getCh().isEmpty()) {
            this.setForeground(Color.BLACK.getColor());
            log.warn("WARN: null foreground with char detected: " + value);
        }
        if (null != cell.getBackground()) {
            this.setBackground(cell.getBackground().getColor());
        } else {
            this.setBackground(Color.WHITE.getColor());
        }

        if (isSelected) {
            int rowStart = table.getSelectedRows()[0];
            int rowEnd = table.getSelectedRows()[table.getSelectedRows().length - 1];
            int colStart = table.getSelectedColumns()[0];
            int colEnd = table.getSelectedColumns()[table.getSelectedColumns().length - 1];
            int top = row == rowStart ? 1 : 0;
            int left = column == colStart ? 1 : 0;
            int bottom = row == rowEnd ? 1 : 0;
            int right = column == colEnd ? 1 : 0;
            this.setBorder(getBorder(top, left, bottom, right));
        } else {
            this.setBorder(null);
        }

        return this;
    }

    private MatteBorder getBorder(int top, int left, int bottom, int right) {
        int borderCode = top * 1000 + left * 100 + bottom * 10 + right;
        MatteBorder border = borderCache.get(borderCode);
        if (border == null) {
            border = BorderFactory.createMatteBorder(top, left, bottom, right,
                    java.awt.Color.BLUE);
            borderCache.put(borderCode, border);
        }
        return border;
    }
}
