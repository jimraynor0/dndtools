package org.toj.dnd.irctoolkit.ui.map.filterpane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

class BtnCancel extends JButton implements ActionListener {

    private static final long serialVersionUID = -5461379820305401981L;

    private MapFilterEditor editorFrame;

    BtnCancel(MapFilterEditor editorFrame) {
        this.editorFrame = editorFrame;

        this.setText("Cancel");
        this.setBounds(173, 93, 89, 23);
        this.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editorFrame.setVisible(false);
    }
}
