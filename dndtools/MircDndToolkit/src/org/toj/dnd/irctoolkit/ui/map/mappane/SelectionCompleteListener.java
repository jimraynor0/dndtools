package org.toj.dnd.irctoolkit.ui.map.mappane;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.map.AddOrUpdateFilterCommand;
import org.toj.dnd.irctoolkit.engine.command.map.EraseWithinAreaCommand;
import org.toj.dnd.irctoolkit.engine.command.map.FillAreaWithCommand;
import org.toj.dnd.irctoolkit.filter.CropFilter;
import org.toj.dnd.irctoolkit.filter.MapFilter;
import org.toj.dnd.irctoolkit.map.MapModel;

public class SelectionCompleteListener implements ListSelectionListener {
    protected Logger log = Logger.getLogger(this.getClass());

    private Map<String, SelectionCompleteListener> listenersCache = new HashMap<String, SelectionCompleteListener>();
    private String mode;

    private MapGridPanel table;
    private boolean selectionFinished = true;

    public SelectionCompleteListener(MapGridPanel mapGridTable) {
        this.table = mapGridTable;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()
                || ((DefaultListSelectionModel) e.getSource())
                        .getMinSelectionIndex() == -1
                || ((DefaultListSelectionModel) e.getSource())
                        .getMaxSelectionIndex() == -1) {
            return;
        }
        if (e.getSource() == table.getSelectionModel()
                && table.getRowSelectionAllowed()) {
            // Column selection changed
            selectionFinished = !selectionFinished;
        } else if (e.getSource() == table.getColumnModel().getSelectionModel()
                && table.getColumnSelectionAllowed()) {
            // Row selection changed
            selectionFinished = !selectionFinished;
        }

        if (selectionFinished) {
            onSelectionFinish();
        }
    }

    private void onSelectionFinish() {
        if (MapGridPanel.MODE_CONTROLLING.equals(mode)) {
            return;
        }
        if (MapGridPanel.MODE_EDITING.equals(mode)) {
            drawMapObjects();
        }
        if (MapGridPanel.MODE_REMOVING.equals(mode)) {
            removeMapObjects();
        }
        if (MapGridPanel.MODE_CROPPING.equals(mode)) {
            createCropFilter();
        }
    }

    protected void drawMapObjects() {
        if (MapModel.getFirstSelection() != null) {
            ToolkitEngine.getEngine().queueCommand(
                    new FillAreaWithCommand(getTable().getSelectedColumns(),
                            getTable().getSelectedRows(), MapModel
                                    .getFirstSelection()));
        }
    }

    private void removeMapObjects() {
        if (MapModel.getFirstSelection() != null) {
            ToolkitEngine.getEngine().queueCommand(
                    new EraseWithinAreaCommand(getTable().getSelectedColumns(),
                            getTable().getSelectedRows(), MapModel
                                    .getCurrentSelection()));
        } else {
            ToolkitEngine.getEngine().queueCommand(
                    new EraseWithinAreaCommand(getTable().getSelectedColumns(),
                            getTable().getSelectedRows()));
        }
    }

    private void createCropFilter() {
        log.debug("creating crop filter...");
        int xMin = getTable().getSelectedColumns()[0];
        int xMax = getTable().getSelectedColumns()[getTable()
                .getSelectedColumns().length - 1];
        int yMin = getTable().getSelectedRows()[0];
        int yMax = getTable().getSelectedRows()[getTable().getSelectedRows().length - 1];
        MapFilter filter = MapFilter.MapFilterFactory
                .createFilter(MapFilter.TYPE_CROP_FILTER, StringUtils.join(
                        new Integer[] { xMin, xMax, yMin, yMax }, ","));
        filter.setActive(true);
        log.debug("filter created: " + filter);
        if (filter != null) {
            ToolkitEngine.getEngine().queueCommand(
                    new AddOrUpdateFilterCommand(filter, CropFilter.class));
        }

        log.debug("restoring prevMode: " + getTable().getPrevMode());
        getTable().setEditMode(getTable().getPrevMode());
    }

    public MapGridPanel getTable() {
        return table;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}