package org.toj.dnd.irctoolkit.ui.map.mappane;

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

    private String mode;

    private MapGridPanel table;
    private boolean rowSelectionFinished = false;
    private boolean colSelectionFinished = false;

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
        if (log.isDebugEnabled()) {
            log.debug("adjusting stopped from "
                    + (e.getSource() == table.getSelectionModel() ? "column"
                            : "row") + " selection change: " + e);
        }
        if (e.getSource() == table.getSelectionModel()) {
            // Column selection changed
            colSelectionFinished = true;
        } else if (e.getSource() == table.getColumnModel().getSelectionModel()) {
            // Row selection changed
            rowSelectionFinished = true;
        }
        if (log.isDebugEnabled()) {
            log.debug("column selection finished: " + colSelectionFinished);
            log.debug("row selection finished: " + rowSelectionFinished);
        }

        if (colSelectionFinished && rowSelectionFinished) {
            onSelectionFinish();
            rowSelectionFinished = false;
            colSelectionFinished = false;
        }
    }

    private void onSelectionFinish() {
        log.info("onSelectionFinish() called, mode: " + mode);
        if (MapGridPanel.MODE_CONTROLLING.equals(mode)) {
            return;
        } else if (MapGridPanel.MODE_EDITING.equals(mode)) {
            drawMapObjects();
        } else if (MapGridPanel.MODE_REMOVING.equals(mode)) {
            removeMapObjects();
        } else if (MapGridPanel.MODE_CROPPING.equals(mode)) {
            createCropFilter();
        }
        getTable().clearSelection();
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
        int xMin = getTable().getSelectedColumns()[0];
        int xMax = getTable().getSelectedColumns()[getTable()
                .getSelectedColumns().length - 1];
        int yMin = getTable().getSelectedRows()[0];
        int yMax = getTable().getSelectedRows()[getTable().getSelectedRows().length - 1];
        MapFilter filter = MapFilter.MapFilterFactory
                .createFilter(MapFilter.TYPE_CROP_FILTER, StringUtils.join(
                        new Integer[] { xMin, xMax, yMin, yMax }, ","));
        filter.setActive(true);
        if (filter != null) {
            ToolkitEngine.getEngine().queueCommand(
                    new AddOrUpdateFilterCommand(filter, CropFilter.class));
        }

        if (log.isDebugEnabled()) {
            log.debug("restoring prevMode: " + getTable().getPrevMode());
        }
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
