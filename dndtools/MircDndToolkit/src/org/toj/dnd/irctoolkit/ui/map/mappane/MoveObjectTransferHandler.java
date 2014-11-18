package org.toj.dnd.irctoolkit.ui.map.mappane;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.ui.MoveMapObjectCommand;

public class MoveObjectTransferHandler extends TransferHandler {

    private static final long serialVersionUID = -2505022959627976196L;

    private JTable table = null;

    public MoveObjectTransferHandler(JTable table, ReadonlyContext context) {
        this.table = table;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        StringBuilder sb = new StringBuilder();
        sb.append(table.getSelectedColumns()[0]).append(",");
        sb.append(
                table.getSelectedColumns()[table.getSelectedColumns().length - 1])
                .append(",");
        sb.append(table.getSelectedRows()[0]).append(",");
        sb.append(table.getSelectedRows()[table.getSelectedRows().length - 1]);
        return new StringSelection(sb.toString());
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
        return MOVE;
    }

    @Override
    public boolean importData(TransferSupport info) {
        String[] params = { "-1", "-1", "-1", "-1" };
        try {
            params = ((String) info.getTransferable().getTransferData(
                    DataFlavor.stringFlavor)).split(",");
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ToolkitEngine.getEngine().queueCommand(
                new MoveMapObjectCommand(Integer.parseInt(params[0]), Integer
                        .parseInt(params[1]), Integer.parseInt(params[2]),
                        Integer.parseInt(params[3]),
                        ((JTable.DropLocation) info.getDropLocation())
                                .getColumn(), ((JTable.DropLocation) info
                                .getDropLocation()).getRow()));

        table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return true;
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int act) {
        table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
