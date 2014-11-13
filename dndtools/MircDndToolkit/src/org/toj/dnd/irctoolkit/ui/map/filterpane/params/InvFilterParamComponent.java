package org.toj.dnd.irctoolkit.ui.map.filterpane.params;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.map.MapModel;
import org.toj.dnd.irctoolkit.map.MapModelList;
import org.toj.dnd.irctoolkit.ui.StyleConstants;
import org.toj.dnd.irctoolkit.ui.map.data.MapModelListWrapper;

public class InvFilterParamComponent extends FilterParamComponent {

    public static final Dimension SUGGESTED_SIZE = new Dimension(400, 400);

    private JTable table;

    public InvFilterParamComponent(String param) {
        table = new JTable();
        table.setModel(new MapModelListWrapper(ToolkitEngine.getEngine()
                .getContext()));

        // table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowHeight(22);
        table.getColumnModel().getColumn(0).setPreferredWidth(34);
        table.getColumnModel().getColumn(0).setMinWidth(22);
        table.getColumnModel().getColumn(1).setPreferredWidth(126);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        table.getColumnModel().getColumn(0)
                .setCellRenderer(new TableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value, boolean isSelected,
                            boolean hasFocus, int row, int column) {
                        MapModel model = (MapModel) value;

                        JPanel panel = new JPanel();
                        panel.setLayout(null);
                        if (isSelected) {
                            panel.setBackground(table.getSelectionBackground());
                        } else {
                            panel.setBackground(table.getBackground());

                        }

                        JLabel label = new JLabel();
                        panel.add(label);

                        // label.setSize(new Dimension(18, 18));
                        label.setBounds(new Rectangle((34 - 18) / 2,
                                (22 - 18) / 2, 18, 18));
                        label.setVerticalAlignment(SwingConstants.CENTER);
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                        label.setBorder(BorderFactory
                                .createLineBorder(java.awt.Color.DARK_GRAY));
                        label.setFont(StyleConstants.ICON_FONT);
                        if (model.getBackground() != null) {
                            label.setBackground(model.getBackground()
                                    .getColor());
                            label.setOpaque(true);
                        } else {
                            label.setForeground(model.getForeground()
                                    .getColor());
                            label.setText(model.getCh());
                        }
                        return panel;
                    }
                });

        if (param != null && !param.isEmpty()) {
            MapModelList modelList = ToolkitEngine.getEngine().getContext()
                    .getModelList();
            for (String ch : param.split(",")) {
                MapModel model = modelList.findModelById(ch);
                if (model == null) {
                    model = modelList.findModelByChOrDesc(ch);
                }
                if (model != null) {
                    table.addRowSelectionInterval(modelList.indexOf(model),
                            modelList.indexOf(model));
                }
            }
            // List<String> params = Arrays.asList(param.split(","));
            // for (MapModel model : modelList) {
            // if (params.contains(model.getCh())
            // || params.contains(model.getDesc())) {
            // table.addRowSelectionInterval(modelList.indexOf(model),
            // modelList.indexOf(model));
            // }
            // }
        }
    }

    @Override
    public Dimension getSuggestedParentSize() {
        return SUGGESTED_SIZE;
    }

    @Override
    public Component getComponent() {
        JScrollPane scrollPane = new JScrollPane(table);
        return scrollPane;
    }

    @Override
    public String retrieveParam() {
        int[] rows = table.getSelectedRows();
        if (rows == null || rows.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i : rows) {
            if (i != rows[0]) {
                sb.append(",");
            }
            MapModel model = ToolkitEngine.getEngine().getContext()
                    .getModelList().get(i);
            sb.append(model.getId());
        }
        return sb.toString();
    }

    @Override
    public Component getTitle() {
        JLabel jLabel = new JLabel("Selected model will be invisible to PCs:");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setPreferredSize(new Dimension(400, 30));
        return jLabel;
    }
}
