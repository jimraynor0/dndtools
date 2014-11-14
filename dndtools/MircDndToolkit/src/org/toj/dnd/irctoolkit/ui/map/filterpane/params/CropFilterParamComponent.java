package org.toj.dnd.irctoolkit.ui.map.filterpane.params;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.ui.StyleConstants;
import org.toj.dnd.irctoolkit.ui.map.mappane.MapCellRenderer;
import org.toj.dnd.irctoolkit.util.AxisUtil;

public class CropFilterParamComponent extends FilterParamComponent {

    private JTable table;

    public CropFilterParamComponent(String param) {
        table = new JTable();
        table.setModel(new MapGridWrapper(ToolkitEngine.getEngine()
                .getContext().getCurrentMap().getUnAxisfiedFilteredMap()));
        table.setRowHeight(StyleConstants.ICON_SIZE.height);
        table.setRowSelectionAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.setColumnSelectionAllowed(true);
        table.setCellSelectionEnabled(true);

        table.setTableHeader(null);

        initColumnModel();

        table.setDefaultRenderer(table.getColumnClass(0), new MapCellRenderer());

        if (param != null && !param.isEmpty()) {
            String[] params = param.split(",");
            int xMin = toNumber(params[0]);
            int xMax = toNumber(params[1]);
            int yMin = toNumber(params[2]);
            int yMax = toNumber(params[3]);
            if (xMin == -1 || xMax == -1 || yMin == -1 || yMax == -1) {
                return;
            }

            table.setColumnSelectionInterval(xMin, xMax);
            table.setRowSelectionInterval(yMin, yMax);
        }
    }

    private int toNumber(String num) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return AxisUtil.toNumber(num);
        }
    }

    private void initColumnModel() {
        for (int i = 0; i < table.getModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setResizable(false);
            table.getColumnModel().getColumn(i)
                    .setPreferredWidth(StyleConstants.ICON_SIZE.width);
            table.getColumnModel().getColumn(i)
                    .setMinWidth(StyleConstants.ICON_SIZE.width);
            table.getColumnModel().getColumn(i)
                    .setMaxWidth(StyleConstants.ICON_SIZE.width);
        }
    }

    @Override
    public Dimension getSuggestedParentSize() {
        return StyleConstants.CROP_FILTER_PARAM_SIZE;
    }

    @Override
    public Component getComponent() {
        JScrollPane scrollPane = new JScrollPane(table);
        return scrollPane;
    }

    @Override
    public String retrieveParam() {
        int xMin = table.getSelectedColumns()[0];
        int xMax = table.getSelectedColumns()[table.getSelectedColumns().length - 1];
        int yMin = table.getSelectedRows()[0];
        int yMax = table.getSelectedRows()[table.getSelectedRows().length - 1];
        return xMin + "," + xMax + "," + yMin + "," + yMax;
    }

    @Override
    public Component getTitle() {
        JLabel jLabel = new JLabel("圈选你想要展现给PC的区域:");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setPreferredSize(StyleConstants.PARAM_TITLE_LABEL_SIZE);
        return jLabel;
    }
}
