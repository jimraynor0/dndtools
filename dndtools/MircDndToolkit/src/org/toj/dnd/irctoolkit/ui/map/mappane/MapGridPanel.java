package org.toj.dnd.irctoolkit.ui.map.mappane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.map.AddOrUpdateFilterCommand;
import org.toj.dnd.irctoolkit.engine.command.map.EraseWithinAreaCommand;
import org.toj.dnd.irctoolkit.engine.command.map.FillAreaWithCommand;
import org.toj.dnd.irctoolkit.engine.observers.MapGridObserver;
import org.toj.dnd.irctoolkit.filter.CropFilter;
import org.toj.dnd.irctoolkit.filter.MapFilter;
import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.map.MapModel;
import org.toj.dnd.irctoolkit.ui.map.data.MapGridWrapper;

public class MapGridPanel extends JTable implements MapGridObserver {
    private static final long serialVersionUID = 1959388793918690973L;

    // private Logger log = Logger.getLogger(this.getClass());

    private static final int CELL_WIDTH_AND_HEIGHT = 22;

    public static final String MODE_EDITING = "editing";
    public static final String MODE_CONTROLLING = "controlling";

    public static boolean isCtrlDown = false;

    private SelectionListener selectionListener;

    public MapGridPanel(ReadonlyContext context) {
        ToolkitEngine.getEngine().addMapGridObserver(this);

        this.setModel(new MapGridWrapper(context));
        this.setRowHeight(CELL_WIDTH_AND_HEIGHT);
        this.setRowSelectionAllowed(false);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.setColumnSelectionAllowed(true);
        this.setCellSelectionEnabled(true);

        this.setTableHeader(null);

        initColumnModel();

        this.setDefaultRenderer(this.getColumnClass(0), new MapCellRenderer());

        this.addMouseListener(new MouseAdapter() {
            private Logger log = Logger.getLogger(this.getClass());

            @Override
            public void mousePressed(MouseEvent e) {
                if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) {
                    isCtrlDown = true;
                }
            }
        });
        this.setTransferHandler(new MoveObjectTransferHandler(this,
                ToolkitEngine.getEngine().getContext()));
        this.setEditMode(MODE_EDITING);
    }

    private void initColumnModel() {
        for (int i = 0; i < this.getModel().getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setResizable(false);
            this.getColumnModel().getColumn(i)
                    .setPreferredWidth(CELL_WIDTH_AND_HEIGHT);
            this.getColumnModel().getColumn(i)
                    .setMinWidth(CELL_WIDTH_AND_HEIGHT);
            this.getColumnModel().getColumn(i)
                    .setMaxWidth(CELL_WIDTH_AND_HEIGHT);
        }
    }

    @Override
    public void update(MapGrid updatedMap) {
        // this.map = updatedMap;
        ((MapGridWrapper) this.getModel()).fireTableStructureChanged();
        initColumnModel();
    }

    private final class SelectionListener implements ListSelectionListener {
        private Logger log = Logger.getLogger(this.getClass());

        private MapGridPanel table;
        private boolean selectionFinished = true;

        public SelectionListener(MapGridPanel mapGridTable) {
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
            } else if (e.getSource() == table.getColumnModel()
                    .getSelectionModel() && table.getColumnSelectionAllowed()) {
                // Row selection changed
                selectionFinished = !selectionFinished;
            }

            if (selectionFinished) {
                if (isCtrlDown) {
                    isCtrlDown = false;
                    log.debug("table.getSelectedColumns(): " + Arrays.toString(table.getSelectedColumns()));
                    log.debug("table.getSelectedRows(): " + Arrays.toString(table.getSelectedRows()));
                    int xMin = table.getSelectedColumns()[0];
                    int xMax = table.getSelectedColumns()[table.getSelectedColumns().length - 1];
                    int yMin = table.getSelectedRows()[0];
                    int yMax = table.getSelectedRows()[table.getSelectedColumns().length - 1];
                    MapFilter filter = MapFilter.MapFilterFactory.createFilter(
                            MapFilter.TYPE_CROP_FILTER, StringUtils.join(new Integer[]{xMin, xMax, yMin, yMax}, ","));
                    filter.setActive(true);
                    if (filter != null) {
                        ToolkitEngine.getEngine().queueCommand(
                                new AddOrUpdateFilterCommand(filter, CropFilter.class));
                    }
                    return;
                }
                if (MapModel.getSelectionMode() == MapModel.MODE_DRAW
                        && MapModel.getFirstSelection() != null) {
                    ToolkitEngine.getEngine().queueCommand(
                            new FillAreaWithCommand(table.getSelectedColumns(),
                                    table.getSelectedRows(), MapModel
                                            .getFirstSelection()));
                } else if (MapModel.getSelectionMode() == MapModel.MODE_ERASE
                        && MapModel.getFirstSelection() != null) {
                    ToolkitEngine.getEngine().queueCommand(
                            new EraseWithinAreaCommand(table
                                    .getSelectedColumns(), table
                                    .getSelectedRows(), MapModel
                                    .getCurrentSelection()));
                } else if (MapModel.getSelectionMode() == MapModel.MODE_ERASE
                        && MapModel.getFirstSelection() == null) {
                    ToolkitEngine.getEngine().queueCommand(
                            new EraseWithinAreaCommand(table
                                    .getSelectedColumns(), table
                                    .getSelectedRows()));
                }
                table.clearSelection();
                table.getColumnModel().getSelectionModel().clearSelection();
            }
        }
    }

    public void setEditMode(String mode) {
        if (mode == MODE_EDITING) {
            this.setDragEnabled(false);
            this.getSelectionModel().addListSelectionListener(
                    getSelectionListener());
            this.getColumnModel().getSelectionModel()
                    .addListSelectionListener(getSelectionListener());
        } else {
            this.getSelectionModel().removeListSelectionListener(
                    getSelectionListener());
            this.getColumnModel().getSelectionModel()
                    .removeListSelectionListener(getSelectionListener());
            this.setDragEnabled(true);
        }
    }

    private SelectionListener getSelectionListener() {
        if (selectionListener == null) {
            selectionListener = new SelectionListener(this);
        }
        return selectionListener;
    }
}
