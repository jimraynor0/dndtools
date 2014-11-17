package org.toj.dnd.irctoolkit.ui.map.data;

import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.observers.FilterListObserver;
import org.toj.dnd.irctoolkit.filter.MapFilter;

public class MapFilterListWrapper extends AbstractTableModel implements
        FilterListObserver {

    private static final long serialVersionUID = 464717390800595912L;

    private List<String> colNames = Arrays.asList(new String[] { "滤镜",
            "参数", "启动" });
    private ReadonlyContext context;

    public MapFilterListWrapper(ReadonlyContext context) {
        this.context = context;
        ToolkitEngine.getEngine().addFilterListObserver(this);
    }

    @Override
    public int getRowCount() {
        return context.getFilterList() == null ? 0 : context.getFilterList()
                .size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return context.getFilterList().get(rowIndex).getType();
        } else if (columnIndex == 1) {
            return context.getFilterList().get(rowIndex).getParams();
        } else {
            return context.getFilterList().get(rowIndex).isActive();
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
        if (columnIndex == 2) {
            return Boolean.class;
        }
        return String.class;
    }

    public MapFilter getModelAt(int index) {
        return context.getFilterList().get(index);
    }

    public int indexOf(MapFilter model) {
        return context.getFilterList().indexOf(model);
    }

    @Override
    public void update(ReadonlyContext context) {
        this.fireTableDataChanged();
    }
}
