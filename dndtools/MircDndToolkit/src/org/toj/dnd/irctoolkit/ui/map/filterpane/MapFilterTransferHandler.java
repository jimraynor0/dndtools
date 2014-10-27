package org.toj.dnd.irctoolkit.ui.map.filterpane;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.util.ArrayList;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.map.CopyFilterCommand;
import org.toj.dnd.irctoolkit.engine.command.map.MoveFilterCommand;
import org.toj.dnd.irctoolkit.filter.MapFilter;

public class MapFilterTransferHandler extends TransferHandler {

    private static final long serialVersionUID = -2505022959627976196L;

    private final DataFlavor localObjectFlavor;
    private int[] oriRowIndice = null;
    private JTable table = null;
    private ReadonlyContext context;

    public MapFilterTransferHandler(JTable table, ReadonlyContext context) {
        this.table = table;
        this.context = context;
        localObjectFlavor = new ActivationDataFlavor(MapFilter[].class,
                DataFlavor.javaJVMLocalObjectMimeType, "Array of items");
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        ArrayList<MapFilter> list = new ArrayList<MapFilter>(
                table.getSelectedRows().length);
        oriRowIndice = table.getSelectedRows();
        for (int i : oriRowIndice) {
            list.add(context.getFilterList().get(i));
        }
        return new DataHandler(list.toArray(new MapFilter[list.size()]),
                localObjectFlavor.getMimeType());
    }

    @Override
    public boolean canImport(TransferSupport info) {
        boolean b = info.isDrop() && info.getComponent() == table;
        table.setCursor(b ? DragSource.DefaultMoveDrop
                : DragSource.DefaultMoveNoDrop);
        return b;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferSupport info) {
        int destIndex = ((JTable.DropLocation) info.getDropLocation()).getRow();
        if (destIndex < 0) {
            destIndex = 0;
        }
        if (destIndex > context.getFilterList().size()) {
            destIndex = context.getFilterList().size();
        }

        table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        if (info.getDropAction() == MOVE) {
            ToolkitEngine.getEngine().queueCommand(
                    new MoveFilterCommand(table.getSelectedRows(), destIndex));
        } else {
            ToolkitEngine.getEngine().queueCommand(
                    new CopyFilterCommand(table.getSelectedRows(), destIndex));
        }
        return true;
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int act) {
        table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
