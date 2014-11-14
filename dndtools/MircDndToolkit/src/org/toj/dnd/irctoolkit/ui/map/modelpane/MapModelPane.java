package org.toj.dnd.irctoolkit.ui.map.modelpane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.configs.DefaultModels;
import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.map.AddOrUpdateModelCommand;
import org.toj.dnd.irctoolkit.engine.command.map.RemoveModelCommand;
import org.toj.dnd.irctoolkit.map.MapModel;
import org.toj.dnd.irctoolkit.ui.StyleConstants;
import org.toj.dnd.irctoolkit.ui.map.data.MapModelListWrapper;
import org.toj.dnd.irctoolkit.ui.map.mappane.MapGridPanel;

import com.alee.laf.combobox.WebComboBoxCellRenderer;

public class MapModelPane extends JPanel {

    private static final long serialVersionUID = 4368011483894796637L;

    private Logger log = Logger.getLogger(this.getClass());

    private static final String NEW_MODEL = "新建地图符号";
    private static final String DELETE_MODEL = "删除地图符号";
    private static final String CANCEL_MODEL_SELECTION = "取消符号选择";
    private static final String LOAD_DEFAULT_MODELS = "载入默认符号";
    private static final String EDIT_MODE_ERASER = "擦除模式";
    private static final String EDIT_MODE_DRAW = "绘图模式";
    private static final String EDIT_MODE_MOVE = "拖动模式";
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
        this.setPreferredSize(new Dimension(StyleConstants.MODEL_PANE_WIDTH,
                451));
        this.mapGridPanel = mapGridPanel;

        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setModel(new MapModelListWrapper(context));
        table.getTableHeader().setPreferredSize(
                StyleConstants.MODEL_LIST_HEADER_SIZE);
        table.setRowHeight(StyleConstants.MODEL_LIST_ROW_HEIGHT);
        table.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(
                        StyleConstants.MODEL_LIST_COLUMN_0_SIZE.width);
        table.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(
                        StyleConstants.MODEL_LIST_COLUMN_1_SIZE.width);
        table.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(
                        StyleConstants.MODEL_LIST_COLUMN_2_SIZE.width);
        table.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(
                        StyleConstants.MODEL_LIST_COLUMN_3_SIZE.width);
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

                        label.setBounds(
                                (StyleConstants.MODEL_LIST_COLUMN_0_SIZE.width - StyleConstants.ICON_SIZE.width) / 2,
                                (StyleConstants.MODEL_LIST_COLUMN_0_SIZE.height - StyleConstants.ICON_SIZE.height) / 2,
                                StyleConstants.ICON_SIZE.width,
                                StyleConstants.ICON_SIZE.height);
                        label.setVerticalAlignment(SwingConstants.CENTER);
                        label.setHorizontalAlignment(SwingConstants.CENTER);

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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        JButton bAdd = new JButton(NEW_MODEL);
        bAdd.setPreferredSize(StyleConstants.BUTTON_SIZE_LONG);
        bAdd.setMargin(StyleConstants.ZERO_INSETS);
        bAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                launchMapModelEditor(-1);
            }
        });

        JButton bDel = new JButton(DELETE_MODEL);
        bDel.setPreferredSize(StyleConstants.BUTTON_SIZE_LONG);
        bDel.setMargin(StyleConstants.ZERO_INSETS);
        bDel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ToolkitEngine.getEngine().queueCommand(
                        new RemoveModelCommand(table.getSelectedRows()));
            }
        });

        JButton bClearSelection = new JButton(CANCEL_MODEL_SELECTION);
        bClearSelection.setPreferredSize(StyleConstants.BUTTON_SIZE_LONG);
        bClearSelection.setMargin(StyleConstants.ZERO_INSETS);
        bClearSelection.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MapModel.setCurrentSelection(null);
                table.clearSelection();
            }
        });
        JComboBox cbEditMode = new JComboBox(new DefaultComboBoxModel(
                EDIT_MODES));
        cbEditMode.setPreferredSize(StyleConstants.EDIT_MODE_DROPDOWN_SIZE);
        ((WebComboBoxCellRenderer) cbEditMode.getRenderer()).getBoxRenderer()
                .setHorizontalAlignment(SwingConstants.CENTER);
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

        JButton bImportModels = new JButton(LOAD_DEFAULT_MODELS);
        bImportModels.setPreferredSize(StyleConstants.BUTTON_SIZE_LONG);
        bImportModels.setMargin(StyleConstants.ZERO_INSETS);
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
        controlPanel.setPreferredSize(StyleConstants.MODEL_PANEL_CONTROL_PANEL_SIZE);
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
