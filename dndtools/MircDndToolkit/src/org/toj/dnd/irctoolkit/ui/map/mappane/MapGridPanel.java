package org.toj.dnd.irctoolkit.ui.map.mappane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.observers.MapGridObserver;
import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.ui.StyleConstants;
import org.toj.dnd.irctoolkit.ui.map.data.MapGridWrapper;

public class MapGridPanel extends JTable implements MapGridObserver {
    private static final long serialVersionUID = 1959388793918690973L;

    private Logger log = Logger.getLogger(this.getClass());

    public static final String MODE_REMOVING = "removing";
    public static final String MODE_EDITING = "editing";
    public static final String MODE_CROPPING = "cropping";
    public static final String MODE_CONTROLLING = "controlling";

    private SelectionCompleteListener selectionListener;
    private String prevMode;

    public MapGridPanel(ReadonlyContext context) {
        ToolkitEngine.getEngine().addMapGridObserver(this);

        this.setModel(new MapGridWrapper(context));
        this.setRowHeight(StyleConstants.ICON_SIZE.height);
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
                    prevMode = selectionListener.getMode();
                    log.debug("mouse pressed with ctrl. prevMode: " + prevMode);
                    setEditMode(MODE_CROPPING);
                }
            }
        });
        this.setTransferHandler(new MoveObjectTransferHandler(this,
                ToolkitEngine.getEngine().getContext()));

        selectionListener = new SelectionCompleteListener(this);
        this.getSelectionModel().addListSelectionListener(selectionListener);
        this.getColumnModel().getSelectionModel()
                .addListSelectionListener(selectionListener);

        this.setEditMode(MODE_EDITING);
    }

    private void initColumnModel() {
        for (int i = 0; i < this.getModel().getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setResizable(false);
            this.getColumnModel().getColumn(i)
                    .setPreferredWidth(StyleConstants.ICON_SIZE.width);
            this.getColumnModel().getColumn(i)
                    .setMinWidth(StyleConstants.ICON_SIZE.width);
            this.getColumnModel().getColumn(i)
                    .setMaxWidth(StyleConstants.ICON_SIZE.width);
        }
    }

    @Override
    public void update(MapGrid updatedMap) {
        // this.map = updatedMap;
        ((MapGridWrapper) this.getModel()).fireTableStructureChanged();
        initColumnModel();
    }

    public void setEditMode(String mode) {
        log.debug("switching to mode: " + mode);
        selectionListener.setMode(mode);
        log.debug("selectionListener set to mode: " + mode);
        if (mode == MODE_CONTROLLING) {
            this.setDragEnabled(true);
            log.debug("drag & drop enabled.");
        } else {
            this.setDragEnabled(false);
            log.debug("drag & drop disabled.");
        }
    }

    public String getPrevMode() {
        return prevMode;
    }
}
