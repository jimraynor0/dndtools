package org.toj.dnd.irctoolkit.ui.map.filterpane.params;

import javax.swing.table.AbstractTableModel;

import org.toj.dnd.irctoolkit.map.MapGridCell;

public class MapGridWrapper extends AbstractTableModel {

    private static final long serialVersionUID = -948035126277511374L;

    // private Logger log = Logger.getLogger(this.getClass());

    private MapGridCell[][] mapGrid;

    public MapGridWrapper(MapGridCell[][] mapGridCells) {
        this.mapGrid = mapGridCells;
    }

    @Override
    public int getColumnCount() {
        return mapGrid.length;
    }

    @Override
    public int getRowCount() {
        return mapGrid[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return mapGrid[columnIndex][rowIndex];
    }
}
