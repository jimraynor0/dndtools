package org.toj.dnd.irctoolkit.ui.map.modelpane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
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
import com.alee.laf.table.WebTableHeaderUI;

public class MapModelPane extends JPanel {

    private static final long serialVersionUID = 4368011483894796637L;

    private Logger log = Logger.getLogger(this.getClass());

    private static final int MODEL_PANE_WIDTH = 280;

    private static final Dimension SIZE_CONTROL_PANEL = new Dimension(
            MODEL_PANE_WIDTH, 64);

    private static final int SIZE_MODEL_LIST_ROW_HEIGHT = 30;
    private static final Dimension SIZE_MODEL_LIST_HEADER = new Dimension(
            MODEL_PANE_WIDTH, SIZE_MODEL_LIST_ROW_HEIGHT);
    private static final Dimension SIZE_MODEL_LIST_COLUMN_0 = new Dimension(44,
            SIZE_MODEL_LIST_ROW_HEIGHT);
    private static final Dimension SIZE_MODEL_LIST_COLUMN_1 = new Dimension(
            115, SIZE_MODEL_LIST_ROW_HEIGHT);
    private static final Dimension SIZE_MODEL_LIST_COLUMN_2 = new Dimension(44,
            SIZE_MODEL_LIST_ROW_HEIGHT);
    private static final Dimension SIZE_MODEL_LIST_COLUMN_3 = new Dimension(56,
            SIZE_MODEL_LIST_ROW_HEIGHT);
    private static final Dimension SIZE_ICON_LABEL = new Dimension(22, 22);

    private static final Dimension SIZE_BUTTON = new Dimension(90, 30);
    private static final Dimension SIZE_DROPDOWN = new Dimension(182, 30);

    private static final Insets INSETS_BUTTON = new Insets(0, 0, 0, 0);

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
        this.setPreferredSize(new Dimension(MODEL_PANE_WIDTH, 451));
        this.mapGridPanel = mapGridPanel;

        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setModel(new MapModelListWrapper(context));
        table.getTableHeader().setPreferredSize(SIZE_MODEL_LIST_HEADER);
        table.setRowHeight(SIZE_MODEL_LIST_ROW_HEIGHT);
        table.getColumnModel().getColumn(0)
                .setPreferredWidth(SIZE_MODEL_LIST_COLUMN_0.width);
        table.getColumnModel().getColumn(1)
                .setPreferredWidth(SIZE_MODEL_LIST_COLUMN_1.width);
        table.getColumnModel().getColumn(2)
                .setPreferredWidth(SIZE_MODEL_LIST_COLUMN_2.width);
        table.getColumnModel().getColumn(3)
                .setPreferredWidth(SIZE_MODEL_LIST_COLUMN_3.width);
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
                                (SIZE_MODEL_LIST_COLUMN_0.width - SIZE_ICON_LABEL.width) / 2,
                                (SIZE_MODEL_LIST_COLUMN_0.height - SIZE_ICON_LABEL.height) / 2,
                                SIZE_ICON_LABEL.width, SIZE_ICON_LABEL.height);
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
        cbEditMode.setPreferredSize(SIZE_DROPDOWN);
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
        controlPanel.setPreferredSize(SIZE_CONTROL_PANEL);
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
