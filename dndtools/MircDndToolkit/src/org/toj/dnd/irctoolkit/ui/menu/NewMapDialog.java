package org.toj.dnd.irctoolkit.ui.menu;

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
import org.toj.dnd.irctoolkit.util.AxisUtil;

public class NewMapDialog extends JDialog {

    private static final long serialVersionUID = -6869822117191223928L;

    private JTextField tWidth;
    private JTextField tHeight;

    public NewMapDialog(JFrame frame) {
        super(frame, true);
        getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lWidth = new JLabel("���");
        getContentPane().add(lWidth);

        tWidth = new JTextField();
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
        tWidth.setColumns(10);

        JLabel lHeight = new JLabel("�߶�");
        getContentPane().add(lHeight);

        tHeight = new JTextField();
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
        tHeight.setColumns(10);

        JButton bOk = new JButton("����");
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
                                "��ǸӴ�ף�Ŀǰֻ��֧�ֱ߳�������"
                                        + AxisUtil.supportsAxisLength()
                                        + "�ĵ�ͼӴ");
                    }
                    ToolkitEngine.getEngine().queueCommand(
                            new NewMapCommand(width, height));
                    setVisible(false);
                } catch (NumberFormatException e1) {
                    ToolkitEngine.getEngine().fireErrorMsgWindow("���Ϳ�Ҫ��������Ӵ��");
                }
            }
        });

        JButton bCancel = new JButton("ȡ��");
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
        setBounds(new Rectangle(parentBound.x + 100, parentBound.y + 100, 160,
                120));
        this.setVisible(true);
    }
}
