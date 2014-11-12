package org.toj.dnd.irctoolkit.ui.map.viewpane;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.map.EraseWithinAreaCommand;
import org.toj.dnd.irctoolkit.engine.command.map.FillAreaWithCommand;
import org.toj.dnd.irctoolkit.engine.observers.PcViewObserver;
import org.toj.dnd.irctoolkit.map.MapModel;
import org.toj.dnd.irctoolkit.ui.map.data.MapViewWrapper;
import org.toj.dnd.irctoolkit.ui.map.mappane.MapCellRenderer;

public class PcViewPanel extends JTable implements PcViewObserver {
    private static final long serialVersionUID = 1959388793918690973L;

    // private Logger log = Logger.getLogger(this.getClass());

    private static final int CELL_WIDTH_AND_HEIGHT = 22;

    public PcViewPanel(ReadonlyContext context) {
        ToolkitEngine.getEngine().addPcViewObserver(this);

        this.setModel(new MapViewWrapper(context));
        this.setRowHeight(CELL_WIDTH_AND_HEIGHT);
        this.setRowSelectionAllowed(false);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.setColumnSelectionAllowed(true);
        this.setCellSelectionEnabled(true);

        this.setTableHeader(null);

        initColumnModel();

        this.setDefaultRenderer(this.getColumnClass(0), new MapCellRenderer());

        // SelectionListener selectionListener = new SelectionListener(this);
        // this.getSelectionModel().addListSelectionListener(selectionListener);
        // this.getColumnModel().getSelectionModel()
        // .addListSelectionListener(selectionListener);
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
    public void update(ReadonlyContext context) {
        ((MapViewWrapper) this.getModel()).fireTableStructureChanged();
        initColumnModel();
    }
}
