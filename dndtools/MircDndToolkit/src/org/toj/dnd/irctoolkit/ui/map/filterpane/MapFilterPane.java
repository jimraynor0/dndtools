package org.toj.dnd.irctoolkit.ui.map.filterpane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.map.RemoveFilterCommand;
import org.toj.dnd.irctoolkit.ui.StyleConstants;
import org.toj.dnd.irctoolkit.ui.map.data.MapFilterListWrapper;

public class MapFilterPane extends JPanel {

    private static final long serialVersionUID = -4456025525662253885L;

    private JTable table;
    private MapFilterEditor modelEditor;

    public MapFilterPane(ReadonlyContext context) {
        setLayout(new BorderLayout(0, 0));
        this.setPreferredSize(new Dimension(240, 451));

        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setModel(new MapFilterListWrapper(context));
        table.setRowHeight(22);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(0).setMinWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setMinWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(40);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setDragEnabled(true);
        table.setDropMode(DropMode.INSERT_ROWS);
        table.setTransferHandler(new MapFilterTransferHandler(table, context));
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    launchMapFilterEditor(table.getSelectedRow());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        JButton bAdd = new JButton("新建地图滤镜");
        bAdd.setPreferredSize(StyleConstants.BUTTON_SIZE_LONG);
        bAdd.setMargin(StyleConstants.ZERO_INSETS);
        bAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                launchMapFilterEditor(-1);
            }
        });

        JButton bDel = new JButton("删除地图滤镜");
        bDel.setPreferredSize(StyleConstants.BUTTON_SIZE_LONG);
        bDel.setMargin(StyleConstants.ZERO_INSETS);
        bDel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ToolkitEngine.getEngine().queueCommand(
                        new RemoveFilterCommand(table.getSelectedRows()));
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(bAdd);
        controlPanel.add(bDel);
        add(controlPanel, BorderLayout.NORTH);
    }

    protected void launchMapFilterEditor(int rowIndex) {
        if (modelEditor == null) {
            modelEditor = new MapFilterEditor();
        }
        if (rowIndex == -1) {
            modelEditor.initWithFilter(null, -1);
        } else {
            modelEditor.initWithFilter(ToolkitEngine.getEngine().getContext()
                    .getFilterList().get(rowIndex), rowIndex);
        }
        modelEditor.adjustPosition();
        modelEditor.setVisible(true);
    }
}
