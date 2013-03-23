package org.toj.dnd.irctoolkit.ui.map.data;

import javax.swing.table.AbstractTableModel;

import org.toj.dnd.irctoolkit.engine.ReadonlyContext;

public class MapViewWrapper extends AbstractTableModel {
    private static final long serialVersionUID = -948035126277511374L;

    // private Logger log = Logger.getLogger(this.getClass());

    private ReadonlyContext context;

    public MapViewWrapper(ReadonlyContext context) {
        this.context = context;
    }

    @Override
    public int getColumnCount() {
        return context.getCurrentMap().getFilteredMap().getWidth();
    }

    @Override
    public int getRowCount() {
        return context.getCurrentMap().getFilteredMap().getHeight();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return context.getCurrentMap().getFilteredMap()
                .getCell(columnIndex, rowIndex);
    }
}
