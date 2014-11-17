package org.toj.dnd.irctoolkit.ui.map.data;

import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.engine.ToolkitContext;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.observers.ModelListObserver;
import org.toj.dnd.irctoolkit.map.MapModel;

public class MapModelListWrapper extends AbstractTableModel implements
        ModelListObserver {

    private static final long serialVersionUID = 464717390800595912L;

    private List<String> colNames = Arrays.asList(new String[] { "图标",
            "描述", "视线", "效果线" });
    private ReadonlyContext context;

    public MapModelListWrapper(ReadonlyContext context) {
        this.context = context;
        ToolkitEngine.getEngine().addModelListObserver(this);
    }

    @Override
    public int getRowCount() {
        return context.getModelList() == null ? 0 : context.getModelList()
                .size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return context.getModelList().get(rowIndex);
        } else if (columnIndex == 1) {
            return context.getModelList().get(rowIndex).getDesc();
        } else if (columnIndex == 2) {
            return context.getModelList().get(rowIndex).isBlocksLineOfSight();
        } else {
            return context.getModelList().get(rowIndex).isBlocksLineOfEffect();
        }
    }

    @Override
    public String getColumnName(int column) {
        return colNames.get(column);
    }

    @Override
    public int findColumn(String columnName) {
        return colNames.indexOf(columnName);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return MapModel.class;
        } else if (columnIndex == 1) {
            return String.class;
        } else {
            return Boolean.class;
        }
    }

    public MapModel getModelAt(int index) {
        return context.getModelList().get(index);
    }

    public int indexOf(MapModel model) {
        return context.getModelList().indexOf(model);
    }

    @Override
    public void update(ToolkitContext context) {
        this.fireTableDataChanged();
    }
}
