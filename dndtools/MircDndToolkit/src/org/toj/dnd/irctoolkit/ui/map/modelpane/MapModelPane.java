package org.toj.dnd.irctoolkit.ui.map.modelpane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import org.toj.dnd.irctoolkit.configs.DefaultModels;
import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.map.AddOrUpdateModelCommand;
import org.toj.dnd.irctoolkit.engine.command.map.RemoveModelCommand;
import org.toj.dnd.irctoolkit.map.MapModel;
import org.toj.dnd.irctoolkit.ui.map.data.MapModelListWrapper;
import org.toj.dnd.irctoolkit.ui.map.mappane.MapGridPanel;

public class MapModelPane extends JPanel {

    private static final long serialVersionUID = 4368011483894796637L;

    private static final Insets INSETS_BUTTON = new Insets(0, 0, 0, 0);
    private static final Dimension SIZE_BUTTON = new Dimension(66, 25);

    private static final String EDIT_MODE_ERASER = "����ģʽ";
    private static final String EDIT_MODE_DRAW = "��ͼģʽ";
    private static final String EDIT_MODE_MOVE = "�϶�ģʽ";
    private static final String[] EDIT_MODES = { EDIT_MODE_DRAW,
            EDIT_MODE_MOVE, EDIT_MODE_ERASER };

    private JTable table;
    private MapModelEditor modelEditor;
    private MapGridPanel mapGridPanel;

    /**
     * Create the panel.
     */
    public MapModelPane(ReadonlyContext context, MapGridPanel mapGridPanel) {
        setLayout(new BorderLayout(0, 0));
        this.setPreferredSize(new Dimension(240, 451));
        this.mapGridPanel = mapGridPanel;

        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setModel(new MapModelListWrapper(context));
        table.setRowHeight(22);
        table.getColumnModel().getColumn(0).setPreferredWidth(34);
        table.getColumnModel().getColumn(0).setMinWidth(22);
        table.getColumnModel().getColumn(1).setPreferredWidth(126);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setDragEnabled(true);
        table.setDropMode(DropMode.INSERT_ROWS);
        table.setTransferHandler(new MapModelTransferHandler(table, context));
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    launchMapModelEditor(table.getSelectedRow());
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (table.getSelectedRow() == -1) {
                            MapModel.setCurrentSelection(null);
                        } else {
                            MapModelListWrapper modelList = ((MapModelListWrapper) table
                                    .getModel());
                            List<MapModel> selectedModels = new ArrayList<MapModel>(
                                    table.getSelectedRows().length);
                            for (int rowIndex : table.getSelectedRows()) {
                                selectedModels.add(modelList
                                        .getModelAt(rowIndex));
                            }
                            MapModel.setCurrentSelection(selectedModels);
                        }
                    }
                });

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
                        label.setBounds(new Rectangle((34 - 20) / 2,
                                (21 - 20) / 2, 20, 20));
                        label.setVerticalAlignment(SwingConstants.CENTER);
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                        // label.setBorder(BorderFactory
                        // .createLineBorder(java.awt.Color.DARK_GRAY));
                        label.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        JButton bAdd = new JButton("�½���ͼ����");
        bAdd.setPreferredSize(SIZE_BUTTON);
        bAdd.setMargin(INSETS_BUTTON);
        bAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                launchMapModelEditor(-1);
            }
        });

        JButton bDel = new JButton("ɾ����ͼ����");
        bDel.setPreferredSize(SIZE_BUTTON);
        bDel.setMargin(INSETS_BUTTON);
        bDel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ToolkitEngine.getEngine().queueCommand(
                        new RemoveModelCommand(table.getSelectedRows()));
            }
        });

        JButton bClearSelection = new JButton("ȡ������ѡ��");
        bClearSelection.setPreferredSize(SIZE_BUTTON);
        bClearSelection.setMargin(INSETS_BUTTON);
        bClearSelection.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MapModel.setCurrentSelection(null);
                table.clearSelection();
            }
        });
        JComboBox cbEditMode = new JComboBox(new DefaultComboBoxModel(
                EDIT_MODES));
        cbEditMode.setPreferredSize(new Dimension(134, 25));
        cbEditMode.setOpaque(true);
        cbEditMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                String mode = ((String) ((JComboBox) e.getSource())
                        .getSelectedItem());
                if (EDIT_MODE_ERASER.equals(mode)) {
                    getMapGridPanel().setEditMode(MapGridPanel.MODE_REMOVING);
                } else if (EDIT_MODE_MOVE.equals(mode)) {
                    getMapGridPanel()
                            .setEditMode(MapGridPanel.MODE_CONTROLLING);
                } else {
                    getMapGridPanel().setEditMode(MapGridPanel.MODE_EDITING);
                }
                getMapGridPanel().clearSelection();
            }
        });

        JButton bImportModels = new JButton("����Ĭ�Ϸ���");
        bImportModels.setPreferredSize(SIZE_BUTTON);
        bImportModels.setMargin(INSETS_BUTTON);
        bImportModels.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ToolkitEngine.getEngine().queueCommand(
                        new AddOrUpdateModelCommand(new DefaultModels()
                                .loadDefaultModels()));
            }
        });
        // bEraser.addActionListener(new ActionListener() {
        //
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // JToggleButton bEraser = (JToggleButton) e.getSource();
        // if (bEraser.isSelected()) {
        // MapModel.setSelectionMode(MapModel.MODE_ERASE);
        // } else {
        // MapModel.setSelectionMode(MapModel.MODE_DRAW);
        // }
        // }
        // });

        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(240, 56));
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
        controlPanel.add(bAdd);
        controlPanel.add(bDel);
        controlPanel.add(bClearSelection);
        controlPanel.add(cbEditMode);
        // controlPanel.add(bEraser);
        // controlPanel.add(bSelectionMode);
        controlPanel.add(bImportModels);
        add(controlPanel, BorderLayout.NORTH);
    }

    private MapGridPanel getMapGridPanel() {
        return this.mapGridPanel;
    }

    protected void launchMapModelEditor(int rowIndex) {
        if (modelEditor == null) {
            modelEditor = new MapModelEditor();
        }
        if (rowIndex == -1) {
            modelEditor.initWithModel(null, -1);
        } else {
            modelEditor.initWithModel(
                    (MapModel) table.getModel().getValueAt(rowIndex, 0),
                    rowIndex);
        }
        modelEditor.adjustBounds();
        modelEditor.setVisible(true);
    }
}
