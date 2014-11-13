package org.toj.dnd.irctoolkit.ui.map.modelpane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.map.AddOrUpdateModelCommand;
import org.toj.dnd.irctoolkit.map.MapModel;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.ui.StyleConstants;

public class MapModelEditor extends JDialog {

    private static final String ICON_ID = "图标Id";
    private static final String ICON = "图标";
    private static final String ICON_DESC = "描述";
    private static final String BLOCK_LOS = "阻挡视线";
    private static final String BLOCK_LOE = "阻挡效果线";

    private static final long serialVersionUID = -5864200205663505539L;

    private JPanel contentPane;
    private JTextField tfId;
    private JTextField tfIcon;
    private JTextField tfDesc;
    private ColorTypeRadioGroup rColorType;
    private JComboBox listColor;
    private JCheckBox checkLoS;
    private JCheckBox checkLoE;
    private MapModel model;
    private int index;

    public MapModelEditor() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lId = new JLabel(ICON_ID);
        lId.setBounds(16, 13, 59, 14);
        contentPane.add(lId);

        tfId = new JTextField();
        tfId.setBounds(70, 10, 230, 20);
        tfId.setEditable(false);
        tfId.setBackground(java.awt.Color.WHITE);
        tfId.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(tfId);
        // tfId.setColumns(10);

        JLabel lIcon = new JLabel(ICON);
        lIcon.setBounds(16, 41, 29, 14);
        contentPane.add(lIcon);

        tfIcon = new JTextField();
        tfIcon.setBounds(45, 38, 22, 22);
        tfIcon.setFont(StyleConstants.ICON_FONT);
        contentPane.add(tfIcon);
        tfIcon.setColumns(10);

        JLabel lDesc = new JLabel(ICON_DESC);
        lDesc.setBounds(73, 41, 61, 14);
        contentPane.add(lDesc);

        tfDesc = new JTextField();
        tfDesc.setBounds(137, 38, 162, 20);
        contentPane.add(tfDesc);
        tfDesc.setColumns(10);

        rColorType = new ColorTypeRadioGroup();
        contentPane.add(rColorType.getRadioFore());
        contentPane.add(rColorType.getRadioBack());

        listColor = new JComboBox(new DefaultComboBoxModel(Color.values()));
        listColor.setBounds(197, 66, 103, 20);
        listColor.setMaximumRowCount(16);
        listColor.setOpaque(true);
        listColor.setRenderer(new ListCellRenderer() {

            private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

            @Override
            public Component getListCellRendererComponent(JList list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                // super.getListCellRendererComponent(list, value, index,
                // isSelected, cellHasFocus);
                Color color = (Color) value;

                JLabel label = (JLabel) defaultRenderer
                        .getListCellRendererComponent(list, value, index,
                                isSelected, cellHasFocus);
                label.setText(color.getName());
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(color.getColor());
                label.setForeground(isDark(color.getColor()) ? java.awt.Color.white
                        : java.awt.Color.black);
                Dimension size = getPreferredSize();
                size.height = 20;
                label.setPreferredSize(size);
                if (isSelected) {
                    label.setBorder(BorderFactory
                            .createLineBorder(java.awt.Color.black));
                }
                return label;
            }

            private boolean isDark(java.awt.Color color) {
                return (color.getRed() + color.getGreen() + color.getBlue()) < 255 * 3 / 2;
            }
        });
        contentPane.add(listColor);

        checkLoS = new JCheckBox(BLOCK_LOS);
        checkLoS.setBounds(10, 93, 124, 23);
        contentPane.add(checkLoS);

        checkLoE = new JCheckBox(BLOCK_LOE);
        checkLoE.setBounds(163, 93, 132, 23);
        contentPane.add(checkLoE);

        contentPane.add(new BtnCancel(this));
        contentPane.add(new BtnOk(this));
    }

    public void initWithModel(MapModel initModel, int index) {
        this.model = initModel;
        if (initModel == null) {
            this.model = new MapModel();
        }

        this.index = index;

        tfId.setText(model.getId());
        tfIcon.setText(model.getCh());
        tfDesc.setText(model.getDesc());

        if (model.getForeground() != null) {
            rColorType.getRadioFore().setSelected(true);
            listColor.setSelectedItem(model.getForeground());
        } else {
            rColorType.getRadioBack().setSelected(true);
            listColor.setSelectedItem(model.getBackground());
        }

        checkLoS.setSelected(model.isBlocksLineOfSight());
        checkLoE.setSelected(model.isBlocksLineOfEffect());
    }

    void addOrUpdateModel() {

        if (ColorTypeRadioGroup.FOREGROUND.equals(rColorType.getSelected())) {
            model.setForeground((Color) listColor.getSelectedItem());
            model.setBackground(null);
        }

        if (ColorTypeRadioGroup.BACKGROUND.equals(rColorType.getSelected())) {
            model.setBackground((Color) listColor.getSelectedItem());
            model.setForeground(null);
            model.setCh("");
        }

        if (model.getForeground() != null) {
            String ic = tfIcon.getText();
            if (ic == null || ic.trim().isEmpty()) {
                ToolkitEngine.getEngine().fireErrorMsgWindow("劳您驾把Icon给填上呗……");
                throw new IllegalArgumentException();
            }
            ic = ic.trim();
            if (isDoubleByteCharacter(ic.substring(0, 1))) {
                ic = ic.substring(0, 1);
            } else {
                if (ic.length() > 2) {
                    ic = ic.substring(0, 2);
                }
                if (ic.length() == 1) {
                    ic = ic + " ";
                }
            }
            model.setCh(ic);
        }

        model.setDesc(tfDesc.getText());

        model.setBlocksLineOfSight(checkLoS.isSelected());
        model.setBlocksLineOfEffect(checkLoE.isSelected());

        ToolkitEngine.getEngine().queueCommand(
                new AddOrUpdateModelCommand(model, index));
    }

    private boolean isDoubleByteCharacter(String ch) {
        return ch.getBytes().length > 1;
    }

    public void adjustBounds() {
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
        bounds.width = 318;
        bounds.height = 189;

        setBounds(bounds);
    }
}
