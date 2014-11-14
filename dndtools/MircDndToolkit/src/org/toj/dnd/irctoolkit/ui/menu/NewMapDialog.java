package org.toj.dnd.irctoolkit.ui.menu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.map.NewMapCommand;
import org.toj.dnd.irctoolkit.ui.StyleConstants;
import org.toj.dnd.irctoolkit.util.AxisUtil;

public class NewMapDialog extends JDialog {

    private static final long serialVersionUID = -6869822117191223928L;

    private JTextField tWidth;
    private JTextField tHeight;

    public NewMapDialog(JFrame frame) {
        super(frame, true);
        getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));

        JLabel lWidth = new JLabel("宽度");
        lWidth.setPreferredSize(new Dimension(25, StyleConstants.INPUT_HEIGHT));
        getContentPane().add(lWidth);

        tWidth = new JTextField();
        tWidth.setPreferredSize(StyleConstants.NEW_MAP_INPUT_SIZE);
        tWidth.setInputVerifier(new InputVerifier() {

            @Override
            public boolean verify(JComponent tf) {
                String value = ((JTextField) tf).getText();
                try {
                    Integer.parseInt(value);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
        getContentPane().add(tWidth);

        JLabel lHeight = new JLabel("高度");
        lHeight.setPreferredSize(new Dimension(25, StyleConstants.INPUT_HEIGHT));
        getContentPane().add(lHeight);

        tHeight = new JTextField();
        tHeight.setPreferredSize(StyleConstants.NEW_MAP_INPUT_SIZE);
        tHeight.setInputVerifier(new InputVerifier() {

            @Override
            public boolean verify(JComponent tf) {
                String value = ((JTextField) tf).getText();
                try {
                    Integer.parseInt(value);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
        getContentPane().add(tHeight);

        JButton bOk = new JButton("创建");
        bOk.setPreferredSize(StyleConstants.BUTTON_SIZE_SMALL);
        getContentPane().add(bOk);
        bOk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int width = Integer.parseInt(tWidth.getText());
                    int height = Integer.parseInt(tHeight.getText());
                    if (width > AxisUtil.supportsAxisLength()
                            || height > AxisUtil.supportsAxisLength()) {
                        ToolkitEngine.getEngine().fireErrorMsgWindow(
                                "抱歉哟亲，目前只能支持边长不超过"
                                        + AxisUtil.supportsAxisLength()
                                        + "的地图哟");
                    }
                    ToolkitEngine.getEngine().queueCommand(
                            new NewMapCommand(width, height));
                    setVisible(false);
                } catch (NumberFormatException e1) {
                    ToolkitEngine.getEngine().fireErrorMsgWindow("长和宽都要是正整数哟亲");
                }
            }
        });

        JButton bCancel = new JButton("取消");
        bCancel.setPreferredSize(StyleConstants.BUTTON_SIZE_SMALL);
        getContentPane().add(bCancel);
        bCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }

    public void launch() {
        Rectangle parentBound = this.getOwner().getBounds();
        setBounds(new Rectangle(parentBound.x + 100, parentBound.y + 100, StyleConstants.NEW_MAP_DIALOG_SIZE.width,
                StyleConstants.NEW_MAP_DIALOG_SIZE.height));
        this.setVisible(true);
    }
}
