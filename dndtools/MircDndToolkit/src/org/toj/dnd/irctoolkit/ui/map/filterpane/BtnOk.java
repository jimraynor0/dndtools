package org.toj.dnd.irctoolkit.ui.map.filterpane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

class BtnOk extends JButton implements ActionListener {

    private static final long serialVersionUID = -2804865348516253414L;

    private MapFilterEditor editorFrame;

    BtnOk(MapFilterEditor editorFrame) {
        this.editorFrame = editorFrame;

        this.setText("确定");
        this.setBounds(45, 93, 89, 23);
        this.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editorFrame.addOrUpdateFilter();
        editorFrame.setVisible(false);
    }
}
