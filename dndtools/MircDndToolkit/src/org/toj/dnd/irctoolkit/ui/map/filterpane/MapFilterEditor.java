package org.toj.dnd.irctoolkit.ui.map.filterpane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.map.AddOrUpdateFilterCommand;
import org.toj.dnd.irctoolkit.filter.MapFilter;
import org.toj.dnd.irctoolkit.ui.map.filterpane.params.FilterParamComponent;

public class MapFilterEditor extends JDialog {

    private static final String[] FILTER_TYPES = new String[] {
            MapFilter.TYPE_INVISIBILITY_FILTER,
            MapFilter.TYPE_LINE_OF_SIGHT_FILTER, MapFilter.TYPE_CROP_FILTER };

    private static final long serialVersionUID = -5864200205663505539L;

    private Logger log = Logger.getLogger(this.getClass());

    private JPanel contentPane;
    private JPanel paramPanel;
    private JComboBox listType;
    private JCheckBox cbActive;
    private int index;
    private String params;

    private FilterParamComponent paramComponent;

    public MapFilterEditor() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setResizable(true);
        setBounds(100, 100, 318, 159);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        initGenericOptions();
        initParamPanel();
    }

    private void initParamPanel() {
        paramPanel = new JPanel(new BorderLayout());
        contentPane.add(paramPanel, BorderLayout.CENTER);
        initParamPanelContent(null);
    }

    private void initGenericOptions() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(103, 60));

        panel.add(new JLabel("滤镜类型: "));
        listType = new JComboBox(new DefaultComboBoxModel(FILTER_TYPES));

        // listType.setBounds(197, 36, 103, 20);
        listType.setMaximumRowCount(16);
        listType.setOpaque(true);
        listType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                initParamPanelContent((String) ((JComboBox) e.getSource())
                        .getSelectedItem());
                paramPanel.updateUI();
            }
        });
        panel.add(listType);

        panel.add(new JLabel("  启动:"));
        this.cbActive = new JCheckBox();
        panel.add(cbActive);

        JLabel placeholder = new JLabel(" ");
        panel.add(placeholder);
        placeholder.setPreferredSize(new Dimension(27, 20));
        panel.add(new BtnOk(this));
        panel.add(new BtnCancel(this));
        contentPane.add(panel, BorderLayout.NORTH);
    }

    private void initParamPanelContent(String filterType) {
        paramPanel.removeAll();
        paramComponent = FilterParamComponent.getFilterParamComponent(
                filterType, this.params);
        if (paramComponent.getTitle() != null) {
            paramPanel.add(paramComponent.getTitle(), BorderLayout.NORTH);
        }
        paramPanel.add(paramComponent.getComponent(), BorderLayout.CENTER);
        adjustSize(paramComponent.getSuggestedParentSize());
    }

    public void initWithFilter(MapFilter initFilter, int index) {
        this.params = null;
        this.listType.setSelectedItem(null);
        this.index = index;
        this.cbActive.setSelected(true);
        this.listType.setEditable(false);
        this.listType.setEnabled(true);

        if (initFilter != null) {
            this.listType.setSelectedItem(initFilter.getType());
            this.listType.setEnabled(false);
            this.cbActive.setSelected(initFilter.isActive());
            this.params = initFilter.getParams();
            initParamPanelContent(initFilter.getType());
        }
    }

    void addOrUpdateFilter() {
        MapFilter filter = MapFilter.MapFilterFactory.createFilter(
                (String) this.listType.getSelectedItem(),
                paramComponent.retrieveParam());
        filter.setActive(this.cbActive.isSelected());
        if (filter != null) {
            ToolkitEngine.getEngine().queueCommand(
                    new AddOrUpdateFilterCommand(filter, index));
        }
    }

    public void adjustSize(Dimension targetSize) {
        setBounds(getBounds().x, getBounds().y, targetSize.width,
                targetSize.height);
    }

    public void adjustPosition() {
        setBounds(calculatePosition());
    }

    private Rectangle calculatePosition() {
        Point pCenter = java.awt.MouseInfo.getPointerInfo().getLocation();
        Rectangle bounds = new Rectangle();
        bounds.x = pCenter.x - getSize().width / 2;
        if (bounds.x < 0) {
            bounds.x = 0;
        }
        bounds.y = pCenter.y - getSize().height / 2;
        if (bounds.y < 0) {
            bounds.y = 0;
        }
        bounds.width = getSize().width;
        bounds.height = getSize().height;
        return bounds;
    }
}
