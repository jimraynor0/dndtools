package org.toj.dnd.irctoolkit.ui.map.data;

import javax.swing.table.AbstractTableModel;

import org.toj.dnd.irctoolkit.engine.ReadonlyContext;

public class MapGridWrapper extends AbstractTableModel {
    private static final long serialVersionUID = -948035126277511374L;

    // private Logger log = Logger.getLogger(this.getClass());

    private ReadonlyContext context;

    public MapGridWrapper(ReadonlyContext context) {
        this.context = context;
    }

    @Override
    public int getColumnCount() {
        return context.getCurrentMap().getWidth();
    }

    @Override
    public int getRowCount() {
        return context.getCurrentMap().getHeight();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return context.getCurrentMap().getCell(columnIndex, rowIndex);
    }
}
